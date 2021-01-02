package me.fixeddev.commandflow.brigadier;

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
import me.fixeddev.commandflow.part.defaults.IntegerPart;
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
import java.util.HashSet;
import java.util.List;
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
        if (optional) {
            argumentBuilder.executes(context -> 1);
        }

        toArgumentBuilder(command.getPart(), argumentBuilder, authorizer);

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

    private ArgumentBuilder<Object, ?> toArgumentBuilder(ArgumentPart part, Authorizer authorizer) {
        if (part instanceof PlayerPart || part instanceof OfflinePlayerPart) {
            return RequiredArgumentBuilder.argument(part.getName(),
                    constructMinecraftArgumentType(NamespacedKey.minecraft("entity"), new Class[]{boolean.class, boolean.class}, true, true));
        } else if (part instanceof BooleanPart) {
            return RequiredArgumentBuilder.argument(part.getName(), BoolArgumentType.bool());
        } else if (part instanceof IntegerPart) {
            return RequiredArgumentBuilder.argument(part.getName(), IntegerArgumentType.integer());
        } else if (part instanceof DoublePart) {
            return RequiredArgumentBuilder.argument(part.getName(), DoubleArgumentType.doubleArg());
        } else {
            if (part instanceof StringPart) {
                StringPart stringPart = (StringPart) part;

                if (stringPart.isConsumeAll()) {
                    return RequiredArgumentBuilder.argument(part.getName(), StringArgumentType.greedyString());
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
                    });
        }
    }

    private ArgumentBuilder<Object, ?> toArgumentBuilder(CommandPart part, ArgumentBuilder<Object, ?> parent, Authorizer authorizer) {
        if (part instanceof PartsWrapper) {

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

            ArgumentBuilder<Object, ?> childBuilder = toArgumentBuilder((ArgumentPart) part, authorizer);

            parent.then(childBuilder);

            return childBuilder;
        }
    }

    private ArgumentBuilder<Object, ?> addArgumentsFromWrapper(PartsWrapper wrapper, ArgumentBuilder<Object, ?> parent, Authorizer authorizer) {
        ArgumentBuilder<Object, ?> current = parent;
        List<ArgumentBuilder<Object, ?>> builders = new ArrayList<>();

        for (CommandPart part : wrapper.getParts()) {
            ArgumentBuilder<Object, ?> childBuilder = toArgumentBuilder(part, current, authorizer);

            // ignore null builders and same builders
            if (childBuilder == null || childBuilder == current) {
                continue;
            }

            builders.add(childBuilder);

            current = childBuilder;
        }

        ArgumentBuilder<Object, ?> last = null;

        for (int i = builders.size() - 1; i >= 0; i--) {
            ArgumentBuilder<Object, ?> child = builders.get(i);

            if (last != null) {
                child.then(last);
            }

            last = child;
        }

        if (last != null) {
            parent.then(last);
        }

        return current;
    }


    private ArgumentBuilder<Object, ?> addSingleWrapper(SinglePartWrapper wrapper, ArgumentBuilder<Object, ?> parent, Authorizer authorizer) {
        return toArgumentBuilder(wrapper.getPart(), parent, authorizer);
    }

    private ArgumentBuilder<Object, ?> addSubCommands(SubCommandPart subCommandPart, ArgumentBuilder<Object, ?> parent, Authorizer authorizer) {
        for (Command subcommand : subCommandPart.getSubCommands()) {
            for (CommandNode<Object> subCommandNode : getCommodoreCommand(subcommand, true, authorizer)) {
                parent.then(subCommandNode);
            }
        }

        return parent;
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
