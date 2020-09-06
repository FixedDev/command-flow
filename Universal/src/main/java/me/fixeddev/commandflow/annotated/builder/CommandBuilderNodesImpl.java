package me.fixeddev.commandflow.annotated.builder;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.action.ReflectiveAction;
import me.fixeddev.commandflow.annotated.action.ValueGetter;
import me.fixeddev.commandflow.annotated.annotation.Named;
import me.fixeddev.commandflow.annotated.annotation.ParentArg;
import me.fixeddev.commandflow.annotated.part.Key;
import me.fixeddev.commandflow.annotated.part.PartFactory;
import me.fixeddev.commandflow.annotated.part.PartInjector;
import me.fixeddev.commandflow.annotated.part.PartModifier;
import me.fixeddev.commandflow.command.Action;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.part.Parts;
import me.fixeddev.commandflow.part.defaults.SubCommandPart;
import net.kyori.text.Component;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandBuilderNodesImpl implements CommandActionNode, CommandDataNode, CommandPartsNode, SubCommandsNode {
    private final Command.Builder builder;
    private final List<Command> subCommands;
    private final PartInjector injector;
    private final List<ValueGetter> partGetters;
    private SubCommandPart.SubCommandHandler subCommandHandler;
    private boolean optional = false;

    public CommandBuilderNodesImpl(String name, PartInjector injector) {
        this.builder = Command.builder(name);
        this.subCommands = new ArrayList<>();
        partGetters = new ArrayList<>();
        this.injector = injector;
    }

    @Override
    public @NotNull CommandPartsNode parts() {
        return this;
    }

    @Override
    public @NotNull CommandActionNode action() {
        return this;
    }

    @Override
    public @NotNull SubCommandsNode action(@NotNull Method method, @NotNull CommandClass commandClass) {
        return action(new ReflectiveAction(partGetters, commandClass, method));
    }

    @Override
    public @NotNull CommandActionNode ofMethodParameters(@NotNull Method method, @NotNull CommandClass handler) {
        for (Parameter parameter : method.getParameters()) {
            PartFactory factory = getFactory(parameter);

            if (factory == null) {
                throw new IllegalStateException("The parameter " + parameter.toString() + " of the method " + method.getName() + " doesn't has a valid factory!");
            }

            String name = getName(parameter);
            List<Annotation> annotations = Arrays.asList(parameter.getAnnotations());

            CommandPart part = factory.createPart(name, annotations);
            PartModifier modifier = injector.getModifiers(parameter.getAnnotations());

            ParentArg arg = parameter.getAnnotation(ParentArg.class);

            if (arg != null) {
                partGetters.add(ValueGetter.forPart(part, arg.value()));

                continue;
            } else {
                partGetters.add(ValueGetter.forPart(part));
            }

            part = modifier.modify(part, annotations);
            builder.addPart(part);
        }

        return this;
    }

    private String getName(Parameter parameter) {
        Named named = parameter.getAnnotation(Named.class);

        return named != null ? named.value() : parameter.getName();
    }

    private PartFactory getFactory(Parameter parameter) {
        PartFactory factory = injector.getFactory(parameter.getType());

        if (factory != null) {
            return factory;
        }

        for (Annotation annotation : parameter.getAnnotations()) {
            factory = injector.getFactory(new Key(parameter.getType(), annotation));

            if (factory != null) {
                break;
            }
        }

        return factory;
    }

    @Override
    public @NotNull SubCommandsNode action(@NotNull Action action) {
        builder.action(action);
        return this;
    }

    @Override
    public @NotNull CommandDataNode aliases(@NotNull List<String> aliases) {
        builder.aliases(aliases);
        return this;
    }

    @Override
    public @NotNull CommandDataNode addAlias(@NotNull String alias) {
        builder.addAlias(alias);
        return this;
    }

    @Override
    public @NotNull CommandDataNode description(@NotNull Component component) {
        builder.description(component);
        return this;
    }

    @Override
    public @NotNull CommandDataNode permission(@NotNull String permission) {
        builder.permission(permission);
        return this;
    }

    @Override
    public @NotNull CommandDataNode permissionMessage(@NotNull Component permissionMessage) {
        builder.permissionMessage(permissionMessage);
        return this;
    }


    @Override
    public @NotNull CommandPartsNode addPart(@NotNull CommandPart part) {
        builder.addPart(part);
        return this;
    }

    @Override
    public SubCommandsNode addSubCommand(@NotNull Command command) {
        subCommands.add(command);
        return this;
    }

    @Override
    public SubCommandsNode addSubCommand(@NotNull CommandDataNode commandDataNode) {
        subCommands.add(commandDataNode.build());
        return this;
    }

    @Override
    public SubCommandsNode setSubCommandHandler(SubCommandPart.SubCommandHandler subCommandHandler) {
        if (subCommandHandler == null) {
            throw new IllegalArgumentException("The provided SubCommandHandler shouldn't be null!");
        }

        this.subCommandHandler = subCommandHandler;
        return this;
    }

    @Override
    public SubCommandsNode optional() {
        optional = true;
        return this;
    }

    @Override
    public Command build() {
        if (!subCommands.isEmpty()) {
            CommandPart part;

            if (subCommandHandler != null) {
                part = new SubCommandPart("subcommand", subCommands, subCommandHandler);
            } else {
                part = new SubCommandPart("subcommand", subCommands);
            }

            if (optional) {
                part = Parts.optional(part);
            }

            builder.addPart(part);
        }


        return builder.build();
    }
}
