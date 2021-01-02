package me.fixeddev.commandflow.brigadier;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.fixeddev.commandflow.Authorizer;
import me.fixeddev.commandflow.bukkit.part.OfflinePlayerPart;
import me.fixeddev.commandflow.bukkit.part.PlayerPart;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.part.PartsWrapper;
import me.fixeddev.commandflow.part.SinglePartWrapper;
import me.fixeddev.commandflow.part.defaults.BooleanPart;
import me.fixeddev.commandflow.part.defaults.DoublePart;
import me.fixeddev.commandflow.part.defaults.IntegerPart;
import me.fixeddev.commandflow.part.defaults.OptionalPart;
import me.fixeddev.commandflow.part.defaults.StringPart;
import me.fixeddev.commandflow.part.defaults.SubCommandPart;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.MinecraftArgumentTypes;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
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
        List<LiteralCommandNode<Object>> commandNodes = getCommodoreCommand(command, bukkitCommand.getCommandManager().getAuthorizer());

        // osd
        commandNodes.forEach(commodore::register);

        Bukkit.getPluginManager().registerEvents(new CommandDataSendListener(bukkitCommand, bukkitCommand::testPermissionSilent), plugin);
    }

    public List<LiteralCommandNode<Object>> getCommodoreCommand(Command command, Authorizer authorizer) {
        return getCommodoreCommand(command, false, authorizer);
    }

    public List<LiteralCommandNode<Object>> getCommodoreCommand(Command command, boolean optional, Authorizer authorizer) {
        LiteralArgumentBuilder<Object> argumentBuilder = LiteralArgumentBuilder.literal(command.getName())
                .requires(new PermissionRequirement(command.getPermission(), authorizer, commodore));
        if (optional) {
            argumentBuilder.executes(context -> 1);
        }

        for (CommandNode<Object> objectCommandNode : convertToNode(shallowFlattening(command.getPart()), authorizer)) {
            argumentBuilder.then(objectCommandNode);
        }

        List<LiteralCommandNode<Object>> argumentBuilders = new ArrayList<>();
        LiteralCommandNode<Object> mainNode = argumentBuilder.build();

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

    private Object shallowFlattening(CommandPart part) {
        if (part instanceof PartsWrapper) {
            return shallowWrapperFlattening((PartsWrapper) part);
        }

        if (part instanceof SinglePartWrapper) {
            return part;
        }

        if (part instanceof SubCommandPart) {
            return shallowSubCommandFlattening((SubCommandPart) part);
        }

        return part;
    }

    private Table<String, String, Object> shallowSubCommandFlattening(SubCommandPart part) {
        Table<String, String, Object> parts = HashBasedTable.create();

        part.getSubCommandMap().forEach((s, command) -> parts.put(s, command.getPermission(), command.getPart()));

        return parts;
    }

    private List<Object> shallowWrapperFlattening(PartsWrapper wrapper) {
        List<Object> parts = new ArrayList<>();

        List<CommandPart> wrapperParts = wrapper.getParts();

        for (int i = wrapperParts.size() - 1; i >= 0; i--) {
            CommandPart childPart = wrapperParts.get(i);

            parts.add(childPart);
        }

        return parts;
    }


    private CommandPart unwrap(SinglePartWrapper wrapper) {
        return wrapper.getPart();
    }

    @SuppressWarnings("unchecked")
    private List<CommandNode<Object>> convertToNode(Object flattened, Authorizer authorizer) {
        if (flattened instanceof Table) {
            Table<String, String, Object> literals = (Table<String, String, Object>) flattened;

            return convertToNode(literals, authorizer);
        }

        if (flattened instanceof List) {
            List<Object> parts = (List<Object>) flattened;

            return convertToNode(parts, authorizer);
        }

        // We know for sure that it isn't any type of wrapper/subcommand since it would be unwind already
        if (flattened instanceof ArgumentPart) {
            ArgumentPart part = (ArgumentPart) flattened;

            // Using this since we can't use set on a singleton list iterator
            return new ArrayList<>(Arrays.asList(convertToNode(part)));
        }

        if (flattened instanceof SinglePartWrapper) {
            return convertToNode(shallowFlattening(unwrap((SinglePartWrapper) flattened)), authorizer);
        }

        if (flattened instanceof PartsWrapper) {
            return convertToNode(shallowWrapperFlattening((PartsWrapper) flattened), authorizer);
        }

        if (flattened instanceof SubCommandPart) {
            return convertToNode(shallowSubCommandFlattening((SubCommandPart) flattened), authorizer);
        }

        // only a not usable part could return this
        return null;
    }

    private List<CommandNode<Object>> convertToNode(List<Object> flattened, Authorizer authorizer) {
        List<CommandNode<Object>> lastNodes = null;

        boolean setAsOptional = false;
        for (Object part : flattened) {
            Object flattenedPart = part;

            if (part instanceof SinglePartWrapper) {
                flattenedPart = shallowFlattening(unwrap((SinglePartWrapper) part));
            }

            List<CommandNode<Object>> nodes = convertToNode(flattenedPart, authorizer);

            if (nodes == null || nodes.isEmpty()) {
                continue;
            }

            if (lastNodes != null) {

                ListIterator<CommandNode<Object>> nodeIterator = nodes.listIterator();

                while (nodeIterator.hasNext()){
                    CommandNode<Object> node = nodeIterator.next();
                    CommandNode<Object> commandNode = node;

                    if (setAsOptional) {
                        commandNode = node.createBuilder()
                                .executes(context -> 1)
                                .build();

                        nodeIterator.set(commandNode);
                        setAsOptional = false;
                    }

                    for (CommandNode<Object> lastNode : lastNodes) {
                        commandNode.addChild(lastNode);
                    }
                }
            }

            if (part instanceof OptionalPart) {
                setAsOptional = true;
            }
            lastNodes = nodes;
        }

        return lastNodes;
    }

    private List<CommandNode<Object>> convertToNode(Table<String, String, Object> literals, Authorizer authorizer) {
        List<CommandNode<Object>> nodes = new ArrayList<>();

        for (Table.Cell<String, String, Object> entry : literals.cellSet()) {
            LiteralArgumentBuilder<Object> argumentBuilder = LiteralArgumentBuilder.literal(entry.getRowKey())
                    .requires(new PermissionRequirement(entry.getColumnKey(), authorizer, commodore));

            Object part = entry.getValue();
            Object flattenedPart = part;

            if(!(part instanceof ArgumentPart) || !(part instanceof List) || !(part instanceof Table)){
                flattenedPart = shallowFlattening((CommandPart) part);
            }

            List<CommandNode<Object>> subCommandNodes = convertToNode(flattenedPart, authorizer);

            if (subCommandNodes != null) {
                for (CommandNode<Object> objectCommandNode : subCommandNodes) {
                    argumentBuilder.then(objectCommandNode);
                }
            }

            nodes.add(argumentBuilder.build());
        }

        return nodes;
    }

    private CommandNode<Object> convertToNode(ArgumentPart part) {
        if (part instanceof PlayerPart || part instanceof OfflinePlayerPart) {
            return RequiredArgumentBuilder.argument(part.getName(),
                    constructMinecraftArgumentType(NamespacedKey.minecraft("entity"),
                            new Class[]{boolean.class, boolean.class}, true, true)
            ).build();
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

            return RequiredArgumentBuilder
                    .argument(part.getName(), StringArgumentType.word())
                    .suggests(new BrigadierSuggestionProvider(commodore, part))
                    .build();
        }
    }


    // Taken from https://github.com/lucko/commodore/blob/master/src/main/java/me/lucko/commodore/file/MinecraftArgumentTypeParser.java#L117
    private ArgumentType<?> constructMinecraftArgumentType(NamespacedKey key, Class<?>[] argTypes, Object... args) {
        try {
            final Constructor<? extends ArgumentType<?>> constructor = MinecraftArgumentTypes.getClassByKey(key).getDeclaredConstructor(argTypes);
            constructor.setAccessible(true);
            return constructor.newInstance(args);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
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
