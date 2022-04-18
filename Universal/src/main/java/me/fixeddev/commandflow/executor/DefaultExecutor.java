package me.fixeddev.commandflow.executor;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.exception.CommandUsage;
import me.fixeddev.commandflow.usage.UsageBuilder;

public class DefaultExecutor implements Executor {

    @Override
    public boolean execute(CommandContext commandContext, UsageBuilder builder) {
        Command toExecute = commandContext.getCommand();

        if (toExecute != null) {
            if (!toExecute.getAction().execute(commandContext)) {
                throw new CommandUsage(builder.getUsage(commandContext))
                        .setCommand(toExecute);
            }
        } else {
            return false;
        }

        return true;
    }

}
