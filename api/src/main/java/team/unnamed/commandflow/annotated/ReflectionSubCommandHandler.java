package team.unnamed.commandflow.annotated;

import team.unnamed.commandflow.command.Command;
import team.unnamed.commandflow.exception.ArgumentException;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.part.defaults.SubCommandPart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionSubCommandHandler implements SubCommandPart.SubCommandHandler {

    private final CommandClass handler;
    private final Method handlerMethod;

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
            Throwable cause = e.getCause();
            if (cause instanceof ArgumentException) {
                throw (ArgumentException) cause;
            }

            throw new ArgumentParseException("Internal error.", cause)
                    .setArgument(context.getPart())
                    .setCommand(context.getContext().getCommand());
        }
    }

}
