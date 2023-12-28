package team.unnamed.commandflow.annotated.builder;

import team.unnamed.commandflow.annotated.CommandClass;
import team.unnamed.commandflow.annotated.action.ReflectiveAction;
import team.unnamed.commandflow.annotated.action.ValueGetter;
import team.unnamed.commandflow.annotated.annotation.Named;
import team.unnamed.commandflow.annotated.annotation.ParentArg;
import team.unnamed.commandflow.annotated.modifier.CommandModifierFactory;
import team.unnamed.commandflow.annotated.part.Key;
import team.unnamed.commandflow.annotated.part.PartFactory;
import team.unnamed.commandflow.annotated.part.PartInjector;
import team.unnamed.commandflow.annotated.part.PartModifier;
import team.unnamed.commandflow.command.Action;
import team.unnamed.commandflow.command.Command;
import team.unnamed.commandflow.command.modifiers.CommandModifier;
import team.unnamed.commandflow.command.modifiers.CommandModifiers;
import team.unnamed.commandflow.command.modifiers.ModifierPhase;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.part.Parts;
import team.unnamed.commandflow.part.defaults.SubCommandPart;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

final class CommandBuilderNodesImpl implements CommandActionNode, CommandDataNode, CommandPartsNode, SubCommandsNode, CommandModifiersNode {

    private final Command.Builder builder;
    private final List<Command> subCommands;
    private final PartInjector injector;
    private final List<ValueGetter> partGetters;
    private SubCommandPart.SubCommandHandler subCommandHandler;
    private boolean optional = false;
    private boolean argumentsOrSubcommand = false;
    private boolean argumentsOrSubcommandReversed = false;
    private Function<CommandPart, CommandPart> modifierFunction = Function.identity();

    private final CommandModifiers.Builder commandModifiersBuilder = CommandModifiers.builder();

    CommandBuilderNodesImpl(String name, PartInjector injector) {
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
    public @NotNull CommandModifiersNode modifiers() {
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
    public @NotNull CommandModifiersNode ofMethod(@NotNull Method method, @NotNull CommandClass handler) {
        List<Annotation> annotations = Arrays.asList(method.getAnnotations());

        for (Annotation annotation : method.getAnnotations()) {
            CommandModifierFactory modifierFactory = injector.getCommandModifierFactory(annotation.getClass());

            if (modifierFactory != null) {
                modifierFactory.modify(commandModifiersBuilder, annotations);
            }
        }

        return this;
    }

    @Override
    public @NotNull CommandModifiersNode addModifiers(List<Annotation> annotations) {

        for (Annotation annotation : annotations) {
            CommandModifierFactory modifierFactory = injector.getCommandModifierFactory(annotation.getClass());

            if (modifierFactory != null) {
                modifierFactory.modify(commandModifiersBuilder, annotations);
            }
        }

        return this;
    }

    @Override
    public @NotNull CommandModifiersNode addModifier(@NotNull ModifierPhase phase, @NotNull CommandModifier modifier) {
        commandModifiersBuilder.addModifier(modifier, phase);

        return this;
    }

    @Override
    public @NotNull CommandActionNode ofMethodParameters(@NotNull Method method, @NotNull CommandClass handler) {
        for (Parameter parameter : method.getParameters()) {
            PartFactory factory = getFactory(parameter);

            if (factory == null) {
                throw new IllegalStateException("The parameter " + parameter + " of the method " + method.getName() + " doesn't has a valid factory!");
            }

            String name = getName(parameter);
            List<Class<? extends Annotation>> annotationTypes = new ArrayList<>();
            List<Annotation> annotations = Arrays.asList(parameter.getAnnotations());

            for (Annotation annotation : parameter.getAnnotations()) {
                annotationTypes.add(annotation.annotationType());
            }

            CommandPart part = factory.createPart(name, annotations);
            PartModifier modifier = injector.getModifiers(annotationTypes.toArray(new Class[0]));

            ParentArg arg = parameter.getAnnotation(ParentArg.class);

            // I don't like this :/
            if (getRawType(parameter) == List.class) {
                if (arg != null) {
                    partGetters.add(ValueGetter.forPartValues(name, arg.value()));
                } else {
                    partGetters.add(ValueGetter.forPartValues(part));
                }
            } else if (getRawType(parameter) == Optional.class) {
                if (arg != null) {
                    partGetters.add(ValueGetter.forOptionalPart(name, arg.value()));
                } else {
                    partGetters.add(ValueGetter.forOptionalPart(part));
                }
            } else {
                if (arg != null) {
                    partGetters.add(ValueGetter.forPart(name, arg.value()));
                } else {
                    partGetters.add(ValueGetter.forPart(part));
                }
            }

            if (arg == null) {
                part = modifier.modify(part, annotations);
                builder.addPart(part);
            }
        }

        return this;
    }

    private Type getRawType(Parameter parameter) {
        if (parameter.getParameterizedType() instanceof ParameterizedType) {
            return ((ParameterizedType) parameter.getParameterizedType()).getRawType();
        }

        return parameter.getType();
    }

    private String getName(Parameter parameter) {
        Named named = parameter.getAnnotation(Named.class);

        return named != null ? named.value() : parameter.getName();
    }

    private PartFactory getFactory(Parameter parameter) {
        PartFactory factory = null;

        Type type = parameter.getType();

        if (parameter.getParameterizedType() instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) parameter.getParameterizedType();

            if (parameterizedType.getRawType() == List.class || parameterizedType.getRawType() == Optional.class) {
                type = parameterizedType.getActualTypeArguments()[0];
            } else {
                type = parameterizedType;
            }
        }

        for (Annotation annotation : parameter.getAnnotations()) {
            factory = injector.getFactory(new Key(type, annotation.annotationType()));

            if (factory != null) {
                break;
            }
        }

        if (factory != null) {
            return factory;
        }

        return injector.getFactory(type);
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
    public @NotNull CommandDataNode usage(@NotNull Component component) {
        builder.usage(component);
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
    public SubCommandsNode setModifiers(Annotation... modifiers) {
        List<Class<? extends Annotation>> annotationTypes = new ArrayList<>();
        List<Annotation> annotations = Arrays.asList(modifiers);

        for (Annotation annotation : modifiers) {
            annotationTypes.add(annotation.annotationType());
        }

        PartModifier modifier = injector.getModifiers(annotationTypes);

        if (modifierFunction != Function.<CommandPart>identity()) {
            Function<CommandPart, CommandPart> oldFunction = modifierFunction;

            modifierFunction = (part) -> modifier.modify(oldFunction.apply(part), annotations);
        } else {
            modifierFunction = (part) -> modifier.modify(part, annotations);
        }

        return this;
    }

    @Override
    public SubCommandsNode argumentsOrSubCommand(boolean reversed) {
        argumentsOrSubcommand = true;
        argumentsOrSubcommandReversed = reversed;
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
                part = new SubCommandPart("subcommand", subCommands, optional && !argumentsOrSubcommand, subCommandHandler);
            } else {
                part = new SubCommandPart("subcommand", subCommands, optional && !argumentsOrSubcommand);
            }

            part = modifierFunction.apply(part);

            if (argumentsOrSubcommand) {
                Command command = builder.build();

                if (argumentsOrSubcommandReversed) {
                    part = Parts.firstMatch(part.getName() + "|" + "arguments", part, command.getPart());
                } else {
                    part = Parts.firstMatch(part.getName() + "|" + "arguments", command.getPart(), part);
                }


                builder.part(part);
            } else {
                builder.addPart(part);
            }

        }

        builder.modifiers(commandModifiersBuilder.build());

        return builder.build();
    }

}
