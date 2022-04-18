package me.fixeddev.commandflow.exception;

import net.kyori.text.Component;

public class CommandUsage extends CommandException {

    public CommandUsage() {
    }

    public CommandUsage(Component message) {
        super(message);
    }

    public CommandUsage(String message) {
        super(message);
    }

    public CommandUsage(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandUsage(Throwable cause) {
        super(cause);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
