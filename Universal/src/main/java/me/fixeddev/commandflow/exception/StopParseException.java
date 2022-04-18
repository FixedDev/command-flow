package me.fixeddev.commandflow.exception;

/**
 * Thrown when we should stop parsing the command, without doing any further processing.
 */
public class StopParseException extends CommandException {

    public StopParseException() {
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
