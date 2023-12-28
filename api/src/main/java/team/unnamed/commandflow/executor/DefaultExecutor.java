package team.unnamed.commandflow.executor;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.command.Command;
import team.unnamed.commandflow.exception.CommandUsage;
import team.unnamed.commandflow.usage.UsageBuilder;

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
