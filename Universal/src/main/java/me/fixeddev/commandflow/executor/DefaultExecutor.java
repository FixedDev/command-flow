package me.fixeddev.commandflow.executor;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.exception.CommandUsage;
import me.fixeddev.commandflow.exception.NoMoreArgumentsException;
import me.fixeddev.commandflow.usage.UsageBuilder;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;

public class DefaultExecutor implements Executor {
    @Override
    public boolean execute(CommandContext commandContext, UsageBuilder builder) {
        Command toExecute = commandContext.getCommand();

        if (toExecute != null) {
            if (!toExecute.getAction().execute(commandContext)) {
                CommandUsage usage = new CommandUsage(builder.getUsage(commandContext));
                usage.setCommand(toExecute);

                throw usage;
            }
        } else {
            return false;
        }

        return true;
    }
}
