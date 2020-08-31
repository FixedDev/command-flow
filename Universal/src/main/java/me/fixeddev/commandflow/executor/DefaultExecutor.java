package me.fixeddev.commandflow.executor;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.exception.CommandUsage;

public class DefaultExecutor implements Executor{
    @Override
    public boolean execute(CommandContext commandContext) {
        Command toExecute = commandContext.getCommand();

        if (toExecute != null) {
            if (!toExecute.getAction().execute(commandContext)) {
                // TODO: send the message
                throw new CommandUsage(toExecute.getPart().getLineRepresentation());
            }
        } else {
            return false;
        }

        return true;
    }
}
