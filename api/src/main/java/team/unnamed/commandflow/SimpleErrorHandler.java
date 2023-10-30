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
