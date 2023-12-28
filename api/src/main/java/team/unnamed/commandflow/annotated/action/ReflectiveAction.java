package team.unnamed.commandflow.annotated.action;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.annotated.CommandClass;
import team.unnamed.commandflow.command.Action;
import team.unnamed.commandflow.exception.CommandException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectiveAction implements Action {

    private final List<ValueGetter> getterList;
    private final CommandClass handler;
    private final Method handlerMethod;

    public ReflectiveAction(List<ValueGetter> getterList,
                            CommandClass handler,
                            Method handlerMethod) {
        this.getterList = getterList;
        this.handler = handler;
        this.handlerMethod = handlerMethod;
    }

    @Override
    public boolean execute(CommandContext context) {
        List<Object> values = new ArrayList<>();

        for (ValueGetter valueGetter : getterList) {
            values.add(valueGetter.getValue(context));
        }

        try {
            boolean accessible = handlerMethod.isAccessible();

            handlerMethod.setAccessible(true);
            Object value = handlerMethod.invoke(handler, values.toArray());
            handlerMethod.setAccessible(accessible);

            if (value == null) {
                return true;
            }

            return (boolean) value;
        } catch (IllegalAccessException | InvocationTargetException e) {
            Throwable throwableToThrow = e;

            if (e.getCause() != null) {
                throwableToThrow = e.getCause();
            }

            if (throwableToThrow instanceof CommandException) {
                throw (CommandException) throwableToThrow;
            }

            throw new CommandException("Internal error.", throwableToThrow);
        }
    }

}
