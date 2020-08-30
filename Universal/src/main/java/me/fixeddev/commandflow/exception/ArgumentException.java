package me.fixeddev.commandflow.exception;

import me.fixeddev.commandflow.part.CommandPart;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.TranslatableComponent;

public class ArgumentException extends RuntimeException {

    private Component message;

    private CommandPart argument;

    public ArgumentException() {
    }

    public ArgumentException(Component message) {
        this((message instanceof TextComponent) ? ((TextComponent) message).content() :
                ((message instanceof TranslatableComponent) ? "%translatable: " + ((TranslatableComponent) message).key() + "%" : null));

        this.message = message;
    }

    public ArgumentException(String message) {
        super(message);

        if (message.startsWith("%translatable:") && message.endsWith("%")) {
            this.message = TranslatableComponent.of(message.substring(14, message.length() - 1));
        } else {
            this.message = TextComponent.of(message);
        }
    }

    public ArgumentException(String message, Throwable cause) {
        super(message, cause);

        if (message.startsWith("%translatable:") && message.endsWith("%")) {
            this.message = TranslatableComponent.of(message.substring(14, message.length() - 1));
        } else {
            this.message = TextComponent.of(message);
        }
    }

    public ArgumentException(Throwable cause) {
        super(cause);
    }

    public void setArgument(CommandPart argument) {
        this.argument = argument;
    }

    public CommandPart getArgument() {
        return argument;
    }

    public Component getMessageComponent() {
        return message;
    }
}
