package team.unnamed.commandflow.annotated;

import me.fixeddev.commandflow.annotated.annotation.*;
import team.unnamed.commandflow.annotated.builder.AnnotatedCommandBuilder;
import team.unnamed.commandflow.annotated.builder.CommandDataNode;
import team.unnamed.commandflow.annotated.builder.CommandModifiersNode;
import team.unnamed.commandflow.annotated.builder.CommandPartsNode;
import team.unnamed.commandflow.annotated.builder.SubCommandsNode;
import team.unnamed.commandflow.annotated.part.PartInjector;
import team.unnamed.commandflow.command.Action;
import team.unnamed.commandflow.command.Command;
import team.unnamed.commandflow.part.defaults.SubCommandPart;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import team.unnamed.commandflow.annotated.annotation.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

final class AnnotatedCommandTreeBuilderImpl implements AnnotatedCommandTreeBuilder {

    private final AnnotatedCommandBuilder builder;
    private final SubCommandInstanceCreator instanceCreator;
    private Function<String, Component> componentParser = component -> {
        // default impl
        if (component.startsWith("%translatable:") && component.endsWith("%")) {
            return Component.translatable(component.substring(14, component.length() - 1));
        } else {
            return Component.text(component);
        }
    };

    AnnotatedCommandTreeBuilderImpl(AnnotatedCommandBuilder builder, SubCommandInstanceCreator instanceCreator) {
        this.builder = builder;
        this.instanceCreator = instanceCreator;
    }

    AnnotatedCommandTreeBuilderImpl(PartInjector injector) {
        builder = AnnotatedCommandBuilder.create(injector);
        instanceCreator = new ReflectionInstanceCreator();
    }

    @Override
    public void setComponentParser(Function<String, Component> componentParser) {
        this.componentParser = requireNonNull(componentParser, "componentParser");
    }

    @Override
    public List<Command> fromClass(CommandClass commandClass) {
        Class<?> clazz = commandClass.getClass();

        team.unnamed.commandflow.annotated.annotation.Command rootCommandAnnotation =
                clazz.getAnnotation(team.unnamed.commandflow.annotated.annotation.Command.class);

        List<Command> commandList = new ArrayList<>();

        Method rootCommandMethod = null;

        for (Method method : clazz.getDeclaredMethods()) {
            team.unnamed.commandflow.annotated.annotation.Command commandAnnotation =
                    method.getAnnotation(team.unnamed.commandflow.annotated.annotation.Command.class);

            if (commandAnnotation == null) {
                continue;
            }

            if (commandAnnotation.names()[0].equals("")) {
                rootCommandMethod = method;

                continue;
            }

            Command command = fromMethod(commandClass, commandAnnotation, method);

            if (command != null) {
                commandList.add(command);
            }
        }

        if (rootCommandAnnotation != null) {
            return createRootCommand(commandClass, clazz, rootCommandAnnotation, commandList, rootCommandMethod);
        }

        return commandList;
    }

    private Command fromMethod(CommandClass commandClass,
                               team.unnamed.commandflow.annotated.annotation.Command commandAnnotation,
                               Method method) {
        if (Modifier.isStatic(method.getModifiers()) || !Modifier.isPublic(method.getModifiers())) {
            return null;
        }

        if (method.getReturnType() != boolean.class
                && method.getReturnType() != Boolean.class &&
                method.getReturnType() != void.class) {
            return null;
        }

        Usage usage = method.getAnnotation(Usage.class);

        String[] names = commandAnnotation.names();

        CommandDataNode dataNode = builder.newCommand(names[0])
                .aliases(Arrays.asList(Arrays.copyOfRange(names, 1, names.length)))
                .permission(commandAnnotation.permission())
                .permissionMessage(componentParser.apply(commandAnnotation.permissionMessage()))
                .description(componentParser.apply(commandAnnotation.desc()));

        if (usage != null) {
            dataNode.usage(componentParser.apply(usage.value()));
        }

        return dataNode.modifiers()
                .ofMethod(method, commandClass)
                .parts()
                .ofMethodParameters(method, commandClass)
                .action(method, commandClass)
                .build();
    }

    @NotNull
    private List<Command> createRootCommand(CommandClass commandClass,
                                            Class<?> clazz,
                                            team.unnamed.commandflow.annotated.annotation.Command rootCommandAnnotation,
                                            List<Command> commandList,
                                            Method rootCommandMethod) {
        String[] names = rootCommandAnnotation.names();

        Usage usage = clazz.getAnnotation(Usage.class);

        if (usage == null && rootCommandMethod != null) {
            usage = rootCommandMethod.getAnnotation(Usage.class);
        }

        CommandDataNode dataNode = builder.newCommand(names[0])
                .aliases(Arrays.asList(Arrays.copyOfRange(names, 1, names.length)))
                .permission(rootCommandAnnotation.permission())
                .permissionMessage(componentParser.apply(rootCommandAnnotation.permissionMessage()))
                .description(componentParser.apply(rootCommandAnnotation.desc()));

        if (usage != null) {
            dataNode.usage(componentParser.apply(usage.value()));
        }

        CommandModifiersNode modifiersNode = dataNode.modifiers();

        if (rootCommandMethod != null) {
            modifiersNode = modifiersNode
                    .ofMethod(rootCommandMethod, commandClass);
        } else {
            modifiersNode = modifiersNode.addModifiers(Arrays.asList(clazz.getAnnotations()));
        }

        CommandPartsNode partsNode = modifiersNode.parts();

        SubCommandsNode subCommandsNode;

        if (rootCommandMethod != null) {
            subCommandsNode = partsNode.ofMethodParameters(rootCommandMethod, commandClass)
                    .action(rootCommandMethod, commandClass);
        } else {
            subCommandsNode = partsNode
                    .action()
                    .action(Action.NULL_ACTION);
        }

        subCommandsNode.setModifiers(clazz.getAnnotations());

        if (!clazz.isAnnotationPresent(Required.class)) {
            subCommandsNode.optional();
        }

        if (clazz.isAnnotationPresent(ArgOrSub.class)) {
            subCommandsNode.argumentsOrSubCommand(clazz.getAnnotation(ArgOrSub.class).value());
        }

        SubCommandClasses classesAnnotation = clazz.getAnnotation(SubCommandClasses.class);

        commandList.addAll(getSubCommandFromClasses(commandClass, clazz, classesAnnotation));

        for (Command command : commandList) {
            subCommandsNode.addSubCommand(command);
        }

        for (Method method : clazz.getMethods()) {
            if (!isHandlerMethod(method)) {
                continue;
            }

            subCommandsNode.setSubCommandHandler(new ReflectionSubCommandHandler(commandClass, method));
        }

        return Collections.singletonList(subCommandsNode.build());
    }

    private List<Command> getSubCommandFromClasses(CommandClass parentInstance,
                                                   Class<?> parentClass,
                                                   SubCommandClasses classesAnnotation) {
        List<Command> commandList = new ArrayList<>();

        if (classesAnnotation == null) {
            return commandList;
        }

        for (Class<? extends CommandClass> subCommandClass : classesAnnotation.value()) {
            if (subCommandClass.equals(parentClass)) {
                continue;
            }

            List<Command> subCommands = fromClass(instanceCreator.createInstance(subCommandClass, parentInstance));
            commandList.addAll(subCommands);
        }

        return commandList;
    }


    private boolean isHandlerMethod(Method method) {
        if (method.getReturnType() != void.class ||
                method.getParameterCount() != 3) {
            return false;
        }

        if (!method.isAnnotationPresent(Handler.class)) {
            return false;
        }

        Class<?>[] parameterTypes = method.getParameterTypes();

        return parameterTypes[0] == SubCommandPart.HandlerContext.class &&
                parameterTypes[1] == String.class &&
                parameterTypes[2] == Command.class;
    }
}
