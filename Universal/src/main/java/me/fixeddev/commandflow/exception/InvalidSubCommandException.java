package me.fixeddev.commandflow.exception;

import net.kyori.adventure.text.Component;

public class InvalidSubCommandException extends ArgumentParseException {

    public InvalidSubCommandException() {
        super();
    }

    public InvalidSubCommandException(String message) {
        super(message);
    }

    public InvalidSubCommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidSubCommandException(Throwable cause) {
        super(cause);
    }

    public InvalidSubCommandException(Component message) {
        super(message);
    }

}
