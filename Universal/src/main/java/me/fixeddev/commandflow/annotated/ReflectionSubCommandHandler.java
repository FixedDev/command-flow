package me.fixeddev.commandflow.annotated;

import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.exception.CommandException;
import me.fixeddev.commandflow.part.SubCommandPart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionSubCommandHandler implements SubCommandPart.SubCommandHandler {
    private CommandClass handler;
    private Method handlerMethod;

    public ReflectionSubCommandHandler(CommandClass handler, Method handlerMethod) {
        this.handler = handler;
        this.handlerMethod = handlerMethod;
    }

    @Override
    public void handle(SubCommandPart.@NotNull HandlerContext context, @NotNull String label, @Nullable Command command) throws ArgumentParseException {
        try {
            boolean accessible = handlerMethod.isAccessible();

            handlerMethod.setAccessible(true);
            handlerMethod.invoke(handler, context, label, command);
            handlerMethod.setAccessible(accessible);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new CommandException("Internal error.", e);
        }
    }
}
