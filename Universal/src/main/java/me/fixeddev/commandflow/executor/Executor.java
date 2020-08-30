package me.fixeddev.commandflow.executor;

import me.fixeddev.commandflow.CommandContext;

public interface Executor {
    boolean execute(CommandContext commandContext);
}
