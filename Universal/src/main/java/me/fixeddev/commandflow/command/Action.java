package me.fixeddev.commandflow.command;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.CommandException;

public interface Action {
    static Action NULL_ACTION = context -> false;

    boolean execute(CommandContext context) throws CommandException;
}
