package me.fixeddev.commandflow.brigadier;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
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
import me.fixeddev.commandflow.part.defaults.StringPart;
import me.fixeddev.commandflow.part.defaults.SubCommandPart;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.MinecraftArgumentTypes;
import org.bukkit.NamespacedKey;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class CommandBrigadierConverter {
    private final Commodore commodore;

    public CommandBrigadierConverter(Commodore commodore) {
        this.commodore = commodore;
    }

    public void registerCommand(Command command, BrigadierCommandWrapper bukkitCommand) {
        LiteralArgumentBuilder<Object> main = getCommodoreCommand(command, bukkitCommand.getCommandManager().getAuthorizer()).get(0);

        commodore.register(bukkitCommand, main);
    }

    public List<LiteralArgumentBuilder<Object>> getCommodoreCommand(Command command, Authorizer authorizer) {
        LiteralArgumentBuilder<Object> argumentBuilder = LiteralArgumentBuilder.literal(command.getName())
                .requires(new PermissionRequirement(command.getPermission(), authorizer, commodore));
        toArgumentBuilder(command.getPart(), argumentBuilder, authorizer);

        List<LiteralArgumentBuilder<Object>> argumentBuilders = new ArrayList<>();

        argumentBuilders.add(argumentBuilder);

        for (String alias : command.getAliases()) {
            argumentBuilders.add(LiteralArgumentBuilder.literal(alias).redirect(argumentBuilder.build())
                    .requires(new PermissionRequirement(command.getPermission(), authorizer, commodore)));
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

            return RequiredArgumentBuilder.argument(part.getName(), StringArgumentType.string());
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
            for (LiteralArgumentBuilder<Object> subCommandNode : getCommodoreCommand(subcommand, authorizer)) {
                parent.then(subCommandNode);
            }

        }

        return parent;
    }
}
