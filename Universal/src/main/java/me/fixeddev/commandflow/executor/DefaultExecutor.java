package me.fixeddev.commandflow.executor;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.exception.CommandUsage;
import me.fixeddev.commandflow.exception.NoMoreArgumentsException;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;

public class DefaultExecutor implements Executor {
    @Override
    public boolean execute(CommandContext commandContext) {
        Command toExecute = commandContext.getCommand();

        if (toExecute != null) {
            try {
                if (!toExecute.getAction().execute(commandContext)) {
                    CommandUsage usage = new CommandUsage(getUsage(commandContext));
                    usage.setCommand(toExecute);

                    throw usage;
                }
            } catch (NoMoreArgumentsException e) {
                CommandUsage usage = new CommandUsage(getUsage(commandContext));
                usage.setCommand(toExecute);

                throw usage;
            }

        } else {
            return false;
        }

        return true;
    }

    private Component getUsage(CommandContext commandContext) {
        Command toExecute = commandContext.getCommand();

        String label = String.join(" ", commandContext.getLabels());

        Component labelComponent = TextComponent.of(label);
        Component partComponents = toExecute.getPart().getLineRepresentation();

        if (partComponents != null) {
            labelComponent = labelComponent.append(TextComponent.of(" ")).append(partComponents);
        }

        return labelComponent;
    }
}
