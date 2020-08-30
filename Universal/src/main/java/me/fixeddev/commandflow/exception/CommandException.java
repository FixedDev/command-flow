package me.fixeddev.commandflow.exception;

import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.part.CommandPart;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.TranslatableComponent;

public class CommandException extends RuntimeException {

    private Component message;

    private Command command;

    public CommandException() {
    }

    public CommandException(Component message) {
        this((message instanceof TextComponent) ? ((TextComponent) message).content() :
                ((message instanceof TranslatableComponent) ? "%translatable: " + ((TranslatableComponent) message).key() + "%" : null));

        this.message = message;
    }

    public CommandException(String message) {
        super(message);

        if (message.startsWith("%translatable:") && message.endsWith("%")) {
            this.message = TranslatableComponent.of(message.substring(14, message.length() - 1));
        } else {
            this.message = TextComponent.of(message);
        }
    }

    public CommandException(String message, Throwable cause) {
        super(message, cause);

        if (message.startsWith("%translatable:") && message.endsWith("%")) {
            this.message = TranslatableComponent.of(message.substring(14, message.length() - 1));
        } else {
            this.message = TextComponent.of(message);
        }
    }

    public CommandException(Throwable cause) {
        super(cause);
    }

    public void setCommand(Command argument) {
        this.command = argument;
    }

    public Command getCommand() {
        return command;
    }

    public Component getMessageComponent() {
        return message;
    }
}
