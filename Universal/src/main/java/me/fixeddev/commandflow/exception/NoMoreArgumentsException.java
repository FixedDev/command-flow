package me.fixeddev.commandflow.exception;

import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.TranslatableComponent;

public class NoMoreArgumentsException extends ArgumentException {

    public NoMoreArgumentsException() {
    }

    public NoMoreArgumentsException(String message) {
        super(message);
    }

    public NoMoreArgumentsException(int size, int position) {
        super(TranslatableComponent.of("argument.no-more", TextComponent.of(size), TextComponent.of(position)));
    }

    public NoMoreArgumentsException(Component message) {
        super(message);
    }

}
