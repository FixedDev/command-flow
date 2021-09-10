package me.fixeddev.commandflow.usage;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.command.Command;
import net.kyori.adventure.text.Component;

public class DefaultUsageBuilder implements UsageBuilder {
    @Override
    public Component getUsage(CommandContext commandContext) {
        Command toExecute = commandContext.getCommand();

        Component usage = toExecute.getUsage();

        if (usage != null) {
            return usage;
        }

        String label = String.join(" ", commandContext.getLabels());

        Component labelComponent = Component.text(label);

        Component partComponents = toExecute.getPart().getLineRepresentation();

        if (partComponents != null) {
            labelComponent = labelComponent.append(Component.text(" ")).append(partComponents);
        }

        return labelComponent;
    }
}
