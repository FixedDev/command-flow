package team.unnamed.commandflow.usage;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.command.Command;
import net.kyori.adventure.text.Component;

public class DefaultUsageBuilder implements UsageBuilder {
    @Override
    public Component getUsage(CommandContext commandContext) {
        Command toExecute = commandContext.getCommand();

        Component usage = toExecute.getUsage();

        String label = String.join(" ", commandContext.getLabels());

        Component labelComponent = Component.text(label);

        if (usage != null) {
            return labelComponent.append(Component.text(" ")).append(usage);
        }

        Component partComponents = toExecute.getPart().getLineRepresentation();

        if (partComponents != null) {
            labelComponent = labelComponent.append(Component.text(" ")).append(partComponents);
        }

        return labelComponent;
    }
}
