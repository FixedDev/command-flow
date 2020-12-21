package me.fixeddev.commandflow.exception;

import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.part.CommandPart;
import net.kyori.text.Component;

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

    public ArgumentException setArgument(CommandPart argument) {
        this.argument = argument;
        return this;
    }

    @Override
    public ArgumentException setCommand(Command argument) {
        // Override the method and return this type
        // so we can use new ArgumentException(...).setCommand(...).setArgument(...)
        super.setCommand(argument);
        return this;
    }

    public CommandPart getArgument() {
        return argument;
    }
}
