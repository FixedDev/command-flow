package team.unnamed.commandflow.executor;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.command.Action;
import team.unnamed.commandflow.command.Command;
import team.unnamed.commandflow.exception.CommandException;
import team.unnamed.commandflow.usage.UsageBuilder;

/**
 * This class has the functionality of actually calling the {@link Action} of the command
 * based on the given {@link CommandContext}.
 */
public interface Executor {

    /**
     * Executes the right {@link Action} based on the given {@link CommandContext}.
     *
     * @param commandContext The {@link CommandContext} of the {@link Command} to execute.
     * @param builder        The {@link UsageBuilder} used to generate an usage text if the {@link Action} returns false or if
     *                       the command execution fails for a valid reason.
     * @return If the {@link Action} for the given {@link CommandContext} could be executed.
     * @throws CommandException If the {@link Action} throws an exception.
     */
    boolean execute(CommandContext commandContext, UsageBuilder builder) throws CommandException;

}
