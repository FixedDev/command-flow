package me.fixeddev.commandflow.usage;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.command.Command;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;

public class DefaultUsageBuilder implements UsageBuilder {
    @Override
    public Component getUsage(CommandContext commandContext) {
        Command toExecute = commandContext.getCommand();

        Component usage = toExecute.getUsage();

        if (usage != null) {
            return usage;
        }

        String label = String.join(" ", commandContext.getLabels());

        Component labelComponent = TextComponent.of(label);

        Component partComponents = toExecute.getPart().getLineRepresentation();

        if (partComponents != null) {
            labelComponent = labelComponent.append(TextComponent.of(" ")).append(partComponents);
        }

        return labelComponent;
    }
}
