package me.fixeddev.commandflow.exception;

import net.kyori.text.Component;

public class NoPermissionsException extends CommandException {

    public NoPermissionsException() {
    }

    public NoPermissionsException(Component message) {
        super(message);
    }

    public NoPermissionsException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
