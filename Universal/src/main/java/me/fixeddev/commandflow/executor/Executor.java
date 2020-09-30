package me.fixeddev.commandflow.executor;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.CommandException;
import me.fixeddev.commandflow.usage.UsageBuilder;

public interface Executor {
    boolean execute(CommandContext commandContext, UsageBuilder builder) throws CommandException;
}
