package me.fixeddev.commandflow.executor;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.CommandException;

public interface Executor {
    boolean execute(CommandContext commandContext) throws CommandException;
}
