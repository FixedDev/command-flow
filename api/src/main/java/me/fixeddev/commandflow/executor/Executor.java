package me.fixeddev.commandflow.executor;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.command.Action;
import me.fixeddev.commandflow.exception.CommandException;
import me.fixeddev.commandflow.usage.UsageBuilder;

/**
 * This class has the functionality of actually calling the {@link Action} of the command
 * based on the given {@link CommandContext}.
 */
public interface Executor {

    /**
     * Executes the right {@link Action} based on the given {@link CommandContext}.
     *
     * @param commandContext The {@link CommandContext} of the {@link me.fixeddev.commandflow.command.Command} to execute.
     * @param builder        The {@link UsageBuilder} used to generate an usage text if the {@link Action} returns false or if
     *                       the command execution fails for a valid reason.
     * @return If the {@link Action} for the given {@link CommandContext} could be executed.
     * @throws CommandException If the {@link Action} throws an exception.
     */
    boolean execute(CommandContext commandContext, UsageBuilder builder) throws CommandException;

}
