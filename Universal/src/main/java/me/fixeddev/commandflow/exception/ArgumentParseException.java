package me.fixeddev.commandflow.exception;

import net.kyori.text.Component;

public class ArgumentParseException extends ArgumentException {
    public ArgumentParseException() {
    }

    public ArgumentParseException(String message) {
        super(message);
    }

    public ArgumentParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArgumentParseException(Throwable cause) {
        super(cause);
    }

    public ArgumentParseException(Component message) {
        super(message);
    }
}
