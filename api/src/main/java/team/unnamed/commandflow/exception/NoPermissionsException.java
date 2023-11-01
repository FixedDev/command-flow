package team.unnamed.commandflow.exception;

import net.kyori.adventure.text.Component;

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
