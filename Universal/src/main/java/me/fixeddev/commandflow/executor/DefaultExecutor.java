package me.fixeddev.commandflow.executor;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.exception.CommandUsage;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;

public class DefaultExecutor implements Executor {
    @Override
    public boolean execute(CommandContext commandContext) {
        Command toExecute = commandContext.getCommand();

        if (toExecute != null) {
            if (!toExecute.getAction().execute(commandContext)) {
                // TODO: send the message
                String label = String.join(" ", commandContext.getLabels());

                Component labelComponent = TextComponent.of(label);
                Component partComponents = toExecute.getPart().getLineRepresentation();

                if (partComponents != null) {
                    labelComponent.mergeStyle(partComponents);
                    labelComponent = labelComponent.append(TextComponent.of(" ")).append(partComponents);
                }

                throw new CommandUsage(labelComponent);
            }
        } else {
            return false;
        }

        return true;
    }
}
