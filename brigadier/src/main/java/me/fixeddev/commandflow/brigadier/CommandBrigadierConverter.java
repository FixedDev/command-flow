package me.fixeddev.commandflow.brigadier;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.fixeddev.commandflow.Authorizer;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.SimpleCommandContext;
import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.bukkit.part.OfflinePlayerPart;
import me.fixeddev.commandflow.bukkit.part.PlayerPart;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.part.PartsWrapper;
import me.fixeddev.commandflow.part.SinglePartWrapper;
import me.fixeddev.commandflow.part.defaults.BooleanPart;
import me.fixeddev.commandflow.part.defaults.DoublePart;
import me.fixeddev.commandflow.part.defaults.FirstMatchPart;
import me.fixeddev.commandflow.part.defaults.IntegerPart;
import me.fixeddev.commandflow.part.defaults.LongPart;
import me.fixeddev.commandflow.part.defaults.OptionalPart;
import me.fixeddev.commandflow.part.defaults.StringPart;
import me.fixeddev.commandflow.part.defaults.SubCommandPart;
import me.fixeddev.commandflow.part.defaults.SwitchPart;
import me.fixeddev.commandflow.part.defaults.ValueFlagPart;
import me.fixeddev.commandflow.part.visitor.CommandPartVisitor;
import me.fixeddev.commandflow.stack.SimpleArgumentStack;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.MinecraftArgumentTypes;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CommandBrigadierConverter {

    private final Commodore commodore;

    public CommandBrigadierConverter(Commodore commodore) {
        this.commodore = commodore;
    }

    public List<LiteralCommandNode<Object>> registerCommand(Command command, Plugin plugin, BrigadierCommandWrapper bukkitCommand) {
        List<LiteralCommandNode<Object>> nodes = getCommodoreCommand(command, bukkitCommand.getCommandManager().getAuthorizer());

        nodes.forEach(commodore::register);

        Bukkit.getPluginManager().registerEvents(new CommandDataSendListener(bukkitCommand, bukkitCommand::testPermissionSilent), plugin);

        return nodes;
    }

    public void unregisterCommand(List<LiteralCommandNode<Object>> nodes) {
        Collection<CommandNode<Object>> rootNodes = commodore.getDispatcher().getRoot().getChildren();

        rootNodes.removeAll(nodes);
    }

    public List<LiteralCommandNode<Object>> getCommodoreCommand(Command command, Authorizer authorizer) {
        return getCommodoreCommand(command, false, authorizer);
    }

    public List<LiteralCommandNode<Object>> getCommodoreCommand(Command command, boolean optional, Authorizer authorizer) {
        LiteralArgumentBuilder<Object> argumentBuilder = LiteralArgumentBuilder.literal(command.getName())
                .requires(new PermissionRequirement(command.getPermission(), authorizer, commodore));

        if (isFirstPartOptional(command.getPart())) {
            argumentBuilder.executes(context -> 1);
        }

        LiteralCommandNode<Object> mainNode = argumentBuilder.build();

        CommandNode<Object> node = convertToNodes(command, authorizer);

        if (node != null) {
            if (node instanceof LiteralCommandNode && (node.getName().equals("valueFlag") || node.getName().equals("Wrapper"))) {
                for (CommandNode<Object> child : node.getChildren()) {
                    mainNode.addChild(child);
                }
            } else {
                mainNode.addChild(node);
            }
        }


        List<LiteralCommandNode<Object>> argumentBuilders = new ArrayList<>();

        argumentBuilders.add(mainNode);

        for (String alias : command.getAliases()) {
            LiteralArgumentBuilder<Object> aliasBuilder = LiteralArgumentBuilder
                    .literal(alias)
                    .redirect(mainNode)
                    .requires(new PermissionRequirement(command.getPermission(), authorizer, commodore));

            if (optional) {
                aliasBuilder = aliasBuilder.executes(context -> 1);
            }

            argumentBuilders.add(aliasBuilder.build());
        }

        return argumentBuilders;
    }

    private CommandNode<Object> convertToNodes(Command command, Authorizer authorizer) {
        return command.getPart().acceptVisitor(new CommandPartVisitor<CommandNode<Object>>() {
            @Override
            public CommandNode<Object> visit(CommandPart part) {
                if (part instanceof SwitchPart) {
                    SwitchPart switchPart = (SwitchPart) part;

                    LiteralCommandNode<Object> shortName = LiteralArgumentBuilder.literal("-" + switchPart.getShortName()).build();
                    LiteralCommandNode<Object> fullName = LiteralArgumentBuilder.literal("--" + switchPart.getName()).build();

                    LiteralArgumentBuilder<Object> builder = LiteralArgumentBuilder.literal("valueFlag");

                    builder.then(shortName);

                    if (switchPart.allowsFullName()) {
                        builder.then(fullName);
                    }

                    return builder.build();
                }

                return null;
            }

            @Override
            public CommandNode<Object> visit(ArgumentPart argumentPart) {
                return CommandBrigadierConverter.this.visit(argumentPart);
            }

            @Override
            public CommandNode<Object> visit(PartsWrapper partsWrapper) {
                return CommandBrigadierConverter.this.visit(partsWrapper, this);
            }

            @Override
            public CommandNode<Object> visit(SinglePartWrapper singlePartWrapper) {
                return CommandBrigadierConverter.this.visit(singlePartWrapper, this);
            }

            @Override
            public CommandNode<Object> visit(SubCommandPart subCommand) {
                return CommandBrigadierConverter.this.visit(subCommand, this, authorizer);
            }
        });
    }

    private void handleSimpleWrapperAdd(MultipleHeadNode<Object> nodeBuilder, CommandNode<Object> node, boolean nextOptional) {
        if (nextOptional) {
            for (CommandNode<Object> child : node.getChildren()) {
                ArgumentBuilder<Object, ?> childBuilder = child.createBuilder();
                childBuilder.executes(context -> 1);

                nodeBuilder.addChild(childBuilder.build());
            }
        } else {
            nodeBuilder.addChild(node.getChildren());
        }
    }

    private boolean handleValueFlagAdd(MultipleHeadNode<Object> nodeBuilder, CommandNode<Object> node, boolean shouldAddNextToTail) {
        nodeBuilder.addChild(node.getChildren());

        if (shouldAddNextToTail) {
            for (CommandNode<Object> child : node.getChildren()) {
                nodeBuilder.addTailPointer(child);
            }
        }

        Iterator<CommandNode<Object>> iterator = node.getChildren().iterator();

        // at least one is present
        CommandNode<Object> mainNode = iterator.next();
        shouldAddNextToTail = true;

        // check if it is a SwitchPart, lol
        if (!mainNode.getChildren().isEmpty()) {
            // it isn't
            nodeBuilder.setHeadPointer(0, mainNode.getChildren().iterator().next());
        }

        return shouldAddNextToTail;
    }

    private boolean handleSimplePartAdding(MultipleHeadNode<Object> nodeBuilder, boolean shouldAddNextToTail, CommandNode<Object> node, boolean nextOptional) {
        if (nextOptional) {
            ArgumentBuilder<Object, ?> childBuilder = node.createBuilder();

            childBuilder.executes(context -> 1);
            node = childBuilder.build();
        }

        nodeBuilder.addChild(node);

        if (shouldAddNextToTail) {
            nodeBuilder.addTailPointer(node);
            shouldAddNextToTail = false;
        }
        return shouldAddNextToTail;
    }

    private boolean isFirstPartOptional(CommandPart part) {
        if (part instanceof PartsWrapper) {
            for (CommandPart commandPart : ((PartsWrapper) part).getParts()) {
                if (!(commandPart instanceof ArgumentPart) && !(commandPart instanceof OptionalPart)) {
                    continue;
                }

                return isFirstPartOptional(commandPart);
            }
        }

        if (part instanceof SinglePartWrapper) {
            if (part instanceof OptionalPart) {
                return true;
            }

            return isFirstPartOptional(((SinglePartWrapper) part).getPart());
        }

        if (part instanceof SubCommandPart) {
            return ((SubCommandPart) part).isOptional();
        }

        return false;
    }

    // Taken from https://github.com/lucko/commodore/blob/master/src/main/java/me/lucko/commodore/file/MinecraftArgumentTypeParser.java#L117
    private static ArgumentType<?> constructMinecraftArgumentType(NamespacedKey key, Class<?>[] argTypes, Object... args) {
        try {
            final Constructor<? extends ArgumentType<?>> constructor = MinecraftArgumentTypes.getClassByKey(key).getDeclaredConstructor(argTypes);
            constructor.setAccessible(true);
            return constructor.newInstance(args);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private CommandNode<Object> toArgumentBuilder(ArgumentPart part) {
        if (part instanceof PlayerPart || part instanceof OfflinePlayerPart) {
            return RequiredArgumentBuilder.argument(part.getName(),
                    constructMinecraftArgumentType(NamespacedKey.minecraft("entity"),
                            new Class[]{boolean.class, boolean.class}, true, true)).build();
        } else if (part instanceof BooleanPart) {
            return RequiredArgumentBuilder.argument(part.getName(), BoolArgumentType.bool()).build();
        } else if (part instanceof IntegerPart) {
            return RequiredArgumentBuilder.argument(part.getName(), IntegerArgumentType.integer()).build();
        } else if (part instanceof DoublePart) {
            return RequiredArgumentBuilder.argument(part.getName(), DoubleArgumentType.doubleArg()).build();
        } else if (part instanceof LongPart) {
            return RequiredArgumentBuilder.argument(part.getName(), LongArgumentType.longArg()).build();
        } else {
            if (part instanceof StringPart) {
                StringPart stringPart = (StringPart) part;

                if (stringPart.isConsumeAll()) {
                    return RequiredArgumentBuilder.argument(part.getName(), StringArgumentType.greedyString()).build();
                } else {
                    return RequiredArgumentBuilder.argument(part.getName(), StringArgumentType.word()).build();
                }
            }

            return RequiredArgumentBuilder.argument(part.getName(), StringArgumentType.word())
                    .suggests((context, builder) -> {
                        CommandSender sender = commodore.getBukkitSender(context.getSource());

                        Namespace namespace = Namespace.create();
                        namespace.setObject(CommandSender.class, BukkitCommandManager.SENDER_NAMESPACE, sender);

                        CommandContext commandContext = new SimpleCommandContext(namespace, new ArrayList<>());

                        List<String> suggestions = part.getSuggestions(commandContext, new SimpleArgumentStack(Collections.singletonList("")));

                        if (suggestions != null) {
                            for (String suggestion : suggestions) {
                                builder.suggest(suggestion);
                            }
                        }

                        return builder.buildFuture();
                    }).build();
        }
    }

    public CommandNode<Object> visit(ArgumentPart argumentPart) {
        return toArgumentBuilder(argumentPart);
    }

    public CommandNode<Object> visit(PartsWrapper partsWrapper, CommandPartVisitor<CommandNode<Object>> visitor) {
        if (partsWrapper instanceof FirstMatchPart) {
            LiteralArgumentBuilder<Object> builder = LiteralArgumentBuilder.literal("Wrapper");

            FirstMatchPart firstMatchPart = (FirstMatchPart) partsWrapper;

            for (CommandPart part : firstMatchPart.getParts()) {
                CommandNode<Object> argumentNode = part.acceptVisitor(visitor);

                if (argumentNode == null) {
                    continue; // not available for brigadier completition.
                }

                if (argumentNode instanceof LiteralCommandNode) {
                    if (argumentNode.getName().equals("Wrapper") || argumentNode.getName().equals("valueFlag")) {
                        // we can't do a lot more lol.
                        argumentNode.getChildren().forEach(builder::then);

                        continue;
                    }
                }

                builder.then(argumentNode);
            }

            return builder.build();
        }

        MultipleHeadNode<Object> nodeBuilder = new MultipleHeadNode<>();
        boolean shouldAddNextToTail = false;

        ListIterator<CommandPart> partsIterator = partsWrapper.getParts().listIterator();

        while (partsIterator.hasNext()) {
            CommandPart part = partsIterator.next();

            CommandNode<Object> node = part.acceptVisitor(visitor);

            if (node == null) {
                continue; // ignore this part, it's not available to be completed in brigadier.
            }

            boolean nextOptional = false;

            if (partsIterator.hasNext()) {
                CommandPart next = partsIterator.next();

                if (isFirstPartOptional(next)) {
                    nextOptional = true;
                }

                partsIterator.previous();
            }

            if (nodeBuilder.getHeadsSize() > 0) {
                if (node instanceof LiteralCommandNode) {
                    if (node.getName().equals("Wrapper")) {
                        handleSimpleWrapperAdd(nodeBuilder, node, nextOptional);
                    } else if (node.getName().equals("valueFlag")) {
                        shouldAddNextToTail = handleValueFlagAdd(nodeBuilder, node, shouldAddNextToTail);
                    } else {
                        if (nextOptional) {
                            ArgumentBuilder<Object, ?> childBuilder = node.createBuilder();

                            childBuilder.executes(context -> 1);
                            node = childBuilder.build();
                        }

                        nodeBuilder.addChild(node);
                    }
                } else {
                    shouldAddNextToTail = handleSimplePartAdding(nodeBuilder, shouldAddNextToTail, node, nextOptional);
                }
            } else {
                if (node.getName().equals("valueFlag")) {
                    shouldAddNextToTail = handleValueFlagAdd(nodeBuilder, node, shouldAddNextToTail);

                    continue;
                }

                shouldAddNextToTail = handleSimplePartAdding(nodeBuilder, shouldAddNextToTail, node, nextOptional);
            }
        }

        return nodeBuilder.getWrappedTail("Wrapper");
    }

    public CommandNode<Object> visit(SinglePartWrapper singlePartWrapper, CommandPartVisitor<CommandNode<Object>> visitor) {
        if (singlePartWrapper instanceof ValueFlagPart) {
            ValueFlagPart valueFlagPart = (ValueFlagPart) singlePartWrapper;
            CommandNode<Object> node = singlePartWrapper.getPart().acceptVisitor(visitor);

            LiteralCommandNode<Object> shortName = LiteralArgumentBuilder.literal("-" + valueFlagPart.getShortName()).then(node).build();
            LiteralCommandNode<Object> fullName = LiteralArgumentBuilder.literal("--" + valueFlagPart.getName()).redirect(shortName).build();

            LiteralArgumentBuilder<Object> builder = LiteralArgumentBuilder.literal("valueFlag");

            builder.then(shortName);

            if (valueFlagPart.allowsFullName()) {
                builder.then(fullName);
            }

            return builder.build();
        }

        return singlePartWrapper.getPart().acceptVisitor(visitor);
    }

    public CommandNode<Object> visit(SubCommandPart subCommand, CommandPartVisitor<CommandNode<Object>> visitor, Authorizer authorizer) {
        LiteralArgumentBuilder<Object> builder = LiteralArgumentBuilder.literal("Wrapper");

        for (Command command : subCommand.getSubCommands()) {
            CommandNode<Object> node = command.getPart().acceptVisitor(visitor);

            LiteralArgumentBuilder<Object> literalNodeBuilder = LiteralArgumentBuilder.literal(command.getName());

            if (command.getPermission() != null) {
                literalNodeBuilder.requires(new PermissionRequirement(command.getPermission(), authorizer, commodore));
            }

            if (node != null) {
                if (node instanceof LiteralCommandNode && (node.getName().equals("valueFlag") || node.getName().equals("Wrapper"))) {
                    for (CommandNode<Object> child : node.getChildren()) {
                        literalNodeBuilder.then(child);
                    }
                } else {
                    literalNodeBuilder.then(node);
                }
            }

            LiteralCommandNode<Object> literalNode = literalNodeBuilder.build();

            builder = builder.then(literalNode);

            for (String alias : command.getAliases()) {
                LiteralCommandNode<Object> aliasNode = LiteralArgumentBuilder.literal(alias).redirect(literalNode).build();

                builder = builder.then(aliasNode);
            }
        }

        return builder.build();
    }

    /**
     * Taken from CommodoreImpl, since we can't use the register method that registers this listener
     * Removes minecraft namespaced argument data, & data for players without permission to view the
     * corresponding commands.
     */
    private static final class CommandDataSendListener implements Listener {

        private final Set<String> aliases;
        private final Set<String> minecraftPrefixedAliases;
        private final Predicate<? super Player> permissionTest;

        CommandDataSendListener(org.bukkit.command.Command pluginCommand, Predicate<? super Player> permissionTest) {
            this.aliases = new HashSet<>(Commodore.getAliases(pluginCommand));
            this.minecraftPrefixedAliases = this.aliases.stream().map(alias -> "minecraft:" + alias).collect(Collectors.toSet());
            this.permissionTest = permissionTest;
        }

        @EventHandler
        public void onCommandSend(PlayerCommandSendEvent e) {
            // always remove 'minecraft:' prefixed aliases added by craftbukkit.
            // this happens because bukkit thinks our injected commands are vanilla commands.
            e.getCommands().removeAll(this.minecraftPrefixedAliases);

            // remove the actual aliases if the player doesn't pass the permission test
            if (!this.permissionTest.test(e.getPlayer())) {
                e.getCommands().removeAll(this.aliases);
            }
        }

    }

}
