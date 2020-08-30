package me.fixeddev.commandflow.executor;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.command.Command;

public class DefaultExecutor implements Executor{
    @Override
    public boolean execute(CommandContext commandContext) {
        Command toExecute = commandContext.getCommand();

        if (toExecute != null) {
            if (!toExecute.getAction().execute(commandContext)) {
                commandContext.getRootCommand().getPart().getLineRepresentation();
            }
        } else {
            return false;
        }

        return true;
    }
}
