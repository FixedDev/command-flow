package me.fixeddev.commandflow;

/**
 * This interface is used to handle errors that occur during the execution of a command, by the usage of error handlers
 * for specific throwable types, with a fallback handler for unhandled errors.
 */
public interface ErrorHandler {

    ErrorConsumer<Throwable> THROW_CONSUMER = (namespace, throwable) -> {
        throw throwable;
    };

    /**
     * Adds an ErrorHandler for a specific throwable type.
     *
     * @param type     The type of throwable to handle.
     * @param consumer The consumer that handles the throwable.
     * @param <T>      The generic type of the throwable to handle.
     */
    <T extends Throwable> void addExceptionHandler(Class<? extends T> type, ErrorConsumer<T> consumer);

    /**
     * Sets the fallback error handler for this ErrorHandler instance, used for all unhandled types.
     *
     * @param consumer The consumer that handles the throwable.
     */
    void setFallbackHandler(ErrorConsumer<Throwable> consumer);

    /**
     * Handles the error by calling the error consumer for the specific throwable type, or the fallback consumer if no
     * specific consumer is found, returning true if the command should be marked as executed, false otherwise.
     *
     * @param namespace The namespace of the command parsing/execution.
     * @param exception The exception that occurred.
     * @return True if the command should be marked as executed, false otherwise.
     * @throws Throwable Any type of exception that should be handled by the caller.
     */
    boolean handleException(Namespace namespace, Throwable exception) throws Throwable;

    static ErrorHandler createHandler() {
        return new SimpleErrorHandler();
    }

    /**
     * A simple implementation of specialized consumer, that allows the handling of specific throwable types.
     *
     * @param <T> The generic type of the throwable to handle.
     */
    interface ErrorConsumer<T> {

        /**
         * Handles the throwable.
         * @param namespace The namespace of the command parsing/execution.
         * @param throwable The throwable to handle.
         * @return True if the command should be marked as executed, false otherwise.
         * @throws Throwable Any type of exception that should be handled by the caller.
         */
        boolean accept(Namespace namespace, T throwable) throws Throwable;

    }

}
