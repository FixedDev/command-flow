package me.fixeddev.commandflow.exception;

import net.kyori.adventure.text.Component;

public class NoMoreArgumentsException extends ArgumentException {

    public NoMoreArgumentsException() {
    }

    public NoMoreArgumentsException(String message) {
        super(message);
    }

    public NoMoreArgumentsException(int size, int position) {
        super(Component.translatable("argument.no-more").args(Component.text(size), Component.text(position)));
    }

    public NoMoreArgumentsException(Component message) {
        super(message);
    }

}
