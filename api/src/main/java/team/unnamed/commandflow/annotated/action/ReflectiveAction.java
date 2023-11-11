/*
 * This file is part of commandflow, licensed under the MIT license
 *
 * Copyright (c) 2020-2023 Unnamed Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
