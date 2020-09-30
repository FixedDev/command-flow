package me.fixeddev.commandflow.annotated.action;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.command.Action;
import me.fixeddev.commandflow.exception.CommandException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectiveAction implements Action {

    private List<ValueGetter> getterList;
    private CommandClass handler;
    private Method handlerMethod;

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

            if(e.getCause() != null){
                throwableToThrow = e.getCause();
            }

            throw new CommandException("Internal error.", throwableToThrow);
        }
    }
}
