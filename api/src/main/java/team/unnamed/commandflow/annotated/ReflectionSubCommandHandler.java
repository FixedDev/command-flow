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
