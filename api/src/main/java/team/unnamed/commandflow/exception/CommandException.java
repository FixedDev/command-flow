package team.unnamed.commandflow.exception;

import team.unnamed.commandflow.command.Command;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;

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

        if (message == null) {
            this.message = null;

            return;
        }

        if (message.startsWith("%translatable:") && message.endsWith("%")) {
            this.message = Component.translatable(message.substring(14, message.length() - 1));
        } else {
            this.message = Component.text(message);
        }
    }

    public CommandException(String message, Throwable cause) {
        super(message, cause);

        if (message.startsWith("%translatable:") && message.endsWith("%")) {
            this.message = Component.translatable(message.substring(14, message.length() - 1));
        } else {
            this.message = Component.text(message);
        }
    }

    public CommandException(Throwable cause) {
        super(cause);
    }

    public CommandException setCommand(Command argument) {
        this.command = argument;

        return this;
    }

    public Command getCommand() {
        return command;
    }

    public Component getMessageComponent() {
        return message;
    }

}
