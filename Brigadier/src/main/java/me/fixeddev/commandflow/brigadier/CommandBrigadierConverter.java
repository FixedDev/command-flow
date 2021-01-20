package me.fixeddev.commandflow.brigadier;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
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
import me.fixeddev.commandflow.part.defaults.OptionalPart;
import me.fixeddev.commandflow.part.defaults.StringPart;
import me.fixeddev.commandflow.part.defaults.SubCommandPart;
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
import java.util.Collections;
import java.util.HashSet;
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

    public void registerCommand(Command command, Plugin plugin, BrigadierCommandWrapper bukkitCommand) {
        getCommodoreCommand(command, bukkitCommand.getCommandManager().getAuthorizer()).forEach(commodore::register);

        Bukkit.getPluginManager().registerEvents(new CommandDataSendListener(bukkitCommand, bukkitCommand::testPermissionSilent), plugin);
    }

    public List<LiteralCommandNode<Object>> getCommodoreCommand(Command command, Authorizer authorizer) {
        return getCommodoreCommand(command, false, authorizer);
    }

    public List<LiteralCommandNode<Object>> getCommodoreCommand(Command command, boolean optional, Authorizer authorizer) {
        LiteralArgumentBuilder<Object> argumentBuilder = LiteralArgumentBuilder.literal(command.getName())
                .requires(new PermissionRequirement(command.getPermission(), authorizer, commodore));

        if(isFirstPartOptional(command.getPart())){
            argumentBuilder.executes(context -> 1);
        }

        LiteralCommandNode<Object> mainNode = argumentBuilder.build();

        toArgumentBuilder(command.getPart(), mainNode, authorizer);

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

            return isFirstPartOptional(part);
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

                        for (String suggestion : part.getSuggestions(commandContext, new SimpleArgumentStack(new ArrayList<>()))) {
                            builder.suggest(suggestion);
                        }

                        return builder.buildFuture();
                    }).build();
        }
    }

    private List<CommandNode<Object>> toArgumentBuilder(CommandPart part, CommandNode<Object> parent, Authorizer authorizer) {
        if (part instanceof PartsWrapper) {
            if(part instanceof FirstMatchPart){
                return addArgumentsFromFirstMatch((FirstMatchPart) part, parent, authorizer);
            }

            return addArgumentsFromWrapper((PartsWrapper) part, parent, authorizer);
        } else if (part instanceof SinglePartWrapper) {
            return addSingleWrapper((SinglePartWrapper) part, parent, authorizer);
        } else if (part instanceof SubCommandPart) {

            return addSubCommands((SubCommandPart) part, parent, authorizer); // same as parent
        } else {
            // Don't try to parse the parts that are not arguments as arguments
            if (!(part instanceof ArgumentPart)) {
                return null;
            }

            CommandNode<Object> childBuilder = toArgumentBuilder((ArgumentPart) part);

            parent.addChild(childBuilder);

            return new ArrayList<>(Collections.singletonList(childBuilder));
        }
    }

    private List<CommandNode<Object>> addArgumentsFromFirstMatch(FirstMatchPart firstMatchPart, CommandNode<Object> parent, Authorizer authorizer){
        List<CommandNode<Object>> nodes = new ArrayList<>();

        for (CommandPart part : firstMatchPart.getParts()) {
            nodes.addAll(toArgumentBuilder(part, parent, authorizer));
        }

        return nodes;
    }

    private List<CommandNode<Object>> addArgumentsFromWrapper(PartsWrapper wrapper, CommandNode<Object> parent, Authorizer authorizer) {
        List<CommandNode<Object>> current = Collections.singletonList(parent);
        List<List<CommandNode<Object>>> builders = new ArrayList<>();

        Multimap<CommandNode<Object>, CommandNode<Object>> optionalsRegistration = MultimapBuilder
                .hashKeys()
                .arrayListValues()
                .build();

        List<CommandNode<Object>> lastNonOptional = Collections.singletonList(parent);
        ListIterator<CommandPart> partsIterator = wrapper.getParts().listIterator();

        while (partsIterator.hasNext()) {
            CommandPart part = partsIterator.next();

            // use the first part
            List<CommandNode<Object>> childBuilder = toArgumentBuilder(part, current.get(0), authorizer);

            if (partsIterator.hasNext()) {
                CommandPart nextPart = partsIterator.next();

                partsIterator.previous();

                if (isOptional(nextPart) && childBuilder != null) {
                    childBuilder.replaceAll(objectCommandNode -> objectCommandNode.createBuilder().executes(context -> 1).build());
                }
            }

            boolean optional = isOptional(part);

            // ignore null builders and same builders
            if (childBuilder == null || childBuilder == current) {
                continue;
            }

            builders.add(childBuilder);

            if (optional) {
                for (CommandNode<Object> commandNode : lastNonOptional) {
                    optionalsRegistration.putAll(commandNode, childBuilder);
                }
            } else {
                lastNonOptional = childBuilder;
            }

            current = childBuilder;
        }


        List<CommandNode<Object>> last = null;

        for (int i = builders.size() - 1; i >= 0; i--) {
            List<CommandNode<Object>> child = builders.get(i);

            if (last != null && !last.isEmpty()) {
                for (CommandNode<Object> childNode : child) {
                    for (CommandNode<Object> lastNode : last) {
                        childNode.addChild(lastNode);
                    }
                } }

            last = child;
        }

        optionalsRegistration.forEach(CommandNode::addChild);

        if (last != null) {
            for (CommandNode<Object> lastNode : last) {
                parent.addChild(lastNode);
            }
        }

        return current;
    }

    private boolean isOptional(CommandPart part) {
        if (part instanceof OptionalPart) {
            return true;
        } else if (part instanceof SubCommandPart) {
            SubCommandPart subCommandPart = (SubCommandPart) part;

            return subCommandPart.isOptional();
        }

        return false;
    }

    private List<CommandNode<Object>> addSingleWrapper(SinglePartWrapper wrapper, CommandNode<Object> parent, Authorizer authorizer) {
        return toArgumentBuilder(wrapper.getPart(), parent, authorizer);
    }

    private List<CommandNode<Object>> addSubCommands(SubCommandPart subCommandPart, CommandNode<Object> parent, Authorizer authorizer) {
        List<CommandNode<Object>> subcommands = new ArrayList<>();

        for (Command subcommand : subCommandPart.getSubCommands()) {
            for (CommandNode<Object> subCommandNode : getCommodoreCommand(subcommand, true, authorizer)) {
                parent.addChild(subCommandNode);

                subcommands.add(subCommandNode);
            }
        }

        return subcommands;
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
