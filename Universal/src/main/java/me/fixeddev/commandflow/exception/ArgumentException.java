package me.fixeddev.commandflow.exception;

import me.fixeddev.commandflow.part.CommandPart;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.TranslatableComponent;

public class ArgumentException extends CommandException {

    private CommandPart argument;

    public ArgumentException() {
    }

    public ArgumentException(Component message) {
        super(message);
    }

    public ArgumentException(String message) {
        super(message);
    }

    public ArgumentException(String message, Throwable cause) {
        super(message, cause);
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
}
