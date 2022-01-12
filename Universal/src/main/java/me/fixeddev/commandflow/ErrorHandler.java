package me.fixeddev.commandflow;

public interface ErrorHandler {

    ErrorConsumer<Throwable> THROW_CONSUMER = (namespace, throwable) -> {
        throw throwable;
    };

    <T extends Throwable> void addExceptionHandler(Class<? extends T> type, ErrorConsumer<T> consumer);

    void setFallbackHandler(ErrorConsumer<Throwable> consumer);

    boolean handleException(Namespace namespace, Throwable exception) throws Throwable;

    static ErrorHandler createHandler() {
        return new SimpleErrorHandler();
    }

    interface ErrorConsumer<T> {
        boolean accept(Namespace namespace, T throwable) throws Throwable;
    }
}
