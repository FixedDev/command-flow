package me.fixeddev.commandflow.command;

import me.fixeddev.commandflow.CommandContext;

public interface Action {
    static Action NULL_ACTION = context -> false;

    boolean execute(CommandContext context);
}
