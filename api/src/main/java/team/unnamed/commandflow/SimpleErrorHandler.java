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
package team.unnamed.commandflow;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SimpleErrorHandler implements ErrorHandler {

    private final Map<Class<?>, ErrorConsumer<?>> exceptionHandlers;
    private ErrorConsumer<Throwable> defaultHandler;

    public SimpleErrorHandler() {
        exceptionHandlers = new HashMap<>();
        defaultHandler = THROW_CONSUMER;
    }

    @Override
    public <T extends Throwable> void addExceptionHandler(Class<? extends T> type, ErrorConsumer<T> consumer) {
        Objects.requireNonNull(consumer, "The error handler can't be null");
        exceptionHandlers.put(type, consumer);
    }

    @Override
    public void setFallbackHandler(ErrorConsumer<Throwable> consumer) {
        defaultHandler = Objects.requireNonNull(consumer, "The fallback error handler can't be null");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean handleException(Namespace namespace, Throwable exception) throws Throwable {
        Class<? extends Throwable> throwableType = exception.getClass();

        ErrorConsumer<Throwable> consumer = (ErrorConsumer<Throwable>) exceptionHandlers.getOrDefault(throwableType, defaultHandler);
        return consumer.accept(namespace, exception);
    }

}
