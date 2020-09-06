package me.fixeddev.commandflow.annotated;

import me.fixeddev.commandflow.annotated.annotation.Handler;
import me.fixeddev.commandflow.annotated.annotation.Required;
import me.fixeddev.commandflow.annotated.annotation.SubCommandClasses;
import me.fixeddev.commandflow.annotated.builder.AnnotatedCommandBuilder;
import me.fixeddev.commandflow.annotated.builder.AnnotatedCommandBuilderImpl;
import me.fixeddev.commandflow.annotated.builder.CommandPartsNode;
import me.fixeddev.commandflow.annotated.builder.SubCommandsNode;
import me.fixeddev.commandflow.annotated.part.PartInjector;
import me.fixeddev.commandflow.command.Action;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.part.SubCommandPart;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.TranslatableComponent;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AnnotatedCommandTreeBuilderImpl implements AnnotatedCommandTreeBuilder {

    private final AnnotatedCommandBuilder builder;
    private final SubCommandInstanceCreator instanceCreator;

    public AnnotatedCommandTreeBuilderImpl(AnnotatedCommandBuilder builder, SubCommandInstanceCreator instanceCreator) {
        this.builder = builder;
        this.instanceCreator = instanceCreator;
    }

    public AnnotatedCommandTreeBuilderImpl(PartInjector injector) {
        builder = new AnnotatedCommandBuilderImpl(injector);
        instanceCreator = new ReflectionInstanceCreator();
    }

    @Override
    public List<Command> fromClass(CommandClass commandClass) {
        Class<?> clazz = commandClass.getClass();

        me.fixeddev.commandflow.annotated.annotation.Command rootCommandAnnotation =
                clazz.getAnnotation(me.fixeddev.commandflow.annotated.annotation.Command.class);

        List<Command> commandList = new ArrayList<>();

        Method rootCommandMethod = null;

        for (Method method : clazz.getDeclaredMethods()) {
            me.fixeddev.commandflow.annotated.annotation.Command commandAnnotation =
                    method.getAnnotation(me.fixeddev.commandflow.annotated.annotation.Command.class);

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
                               me.fixeddev.commandflow.annotated.annotation.Command commandAnnotation,
                               Method method) {
        if (Modifier.isStatic(method.getModifiers()) || !Modifier.isPublic(method.getModifiers())) {
            return null;
        }

        if (method.getReturnType() != boolean.class
                && method.getReturnType() != Boolean.class &&
                method.getReturnType() != void.class) {
            return null;
        }

        String[] names = commandAnnotation.names();

        return builder.newCommand(names[0])
                .aliases(Arrays.asList(Arrays.copyOfRange(names, 1, names.length)))
                .permission(commandAnnotation.permission())
                .permissionMessage(fromString(commandAnnotation.permissionMessage()))
                .description(fromString(commandAnnotation.desc()))
                .parts()
                .ofMethodParameters(method, commandClass)
                .action(method, commandClass)
                .build();
    }

    @NotNull
    private List<Command> createRootCommand(CommandClass commandClass,
                                            Class<?> clazz,
                                            me.fixeddev.commandflow.annotated.annotation.Command rootCommandAnnotation,
                                            List<Command> commandList,
                                            Method rootCommandMethod) {
        String[] names = rootCommandAnnotation.names();

        CommandPartsNode partsNode = builder.newCommand(names[0])
                .aliases(Arrays.asList(Arrays.copyOfRange(names, 1, names.length)))
                .permission(rootCommandAnnotation.permission())
                .permissionMessage(fromString(rootCommandAnnotation.permissionMessage()))
                .description(fromString(rootCommandAnnotation.desc()))
                .parts();

        SubCommandsNode subCommandsNode;

        if (rootCommandMethod != null) {
            subCommandsNode = partsNode.ofMethodParameters(rootCommandMethod, commandClass)
                    .action(rootCommandMethod, commandClass);
        } else {
            subCommandsNode = partsNode
                    .action()
                    .action(Action.NULL_ACTION);
        }

        if(!clazz.isAnnotationPresent(Required.class)) {
            subCommandsNode.optional();
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

            try {
                List<Command> subCommands = fromClass(instanceCreator.createInstance(subCommandClass, parentInstance));
                commandList.addAll(subCommands);
            } catch (RuntimeException ignored) {
            }
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

    private Component fromString(String component) {
        if (component.startsWith("%translatable:") && component.endsWith("%")) {
            return TranslatableComponent.of(component.substring(14, component.length() - 1));
        } else {
            return TextComponent.of(component);
        }
    }
}
