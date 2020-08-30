package me.fixeddev.commandflow.exception;

import net.kyori.text.Component;

public class NoMoreArgumentsException extends ArgumentException {

    public NoMoreArgumentsException() {
    }

    public NoMoreArgumentsException(String message) {
        super(message);
    }

    public NoMoreArgumentsException(int size, int position) {
        super("No more arguments were found, size: " + size + " position: " + position);
    }

    public NoMoreArgumentsException(Component message) {
        super(message);
    }
}
