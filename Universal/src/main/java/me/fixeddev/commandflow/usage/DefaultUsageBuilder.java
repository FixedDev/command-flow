package me.fixeddev.commandflow.usage;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.ComponentUtil;
import me.fixeddev.commandflow.command.Command;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;

import java.util.regex.Pattern;

public class DefaultUsageBuilder implements UsageBuilder {

    @Override
    public Component getUsage(CommandContext commandContext) {
        Command toExecute = commandContext.getCommand();

        Component usage = toExecute.getUsage();

        String label = String.join(" ", commandContext.getLabels());

        TextComponent labelComponent = TextComponent.of(label);

        if (usage != null) {
            return ComponentUtil.basicReplace(usage, Pattern.compile("<command>", Pattern.LITERAL), labelComponent);
        }

        Component partComponents = toExecute.getPart().getLineRepresentation();

        if (partComponents != null) {
            labelComponent = labelComponent.append(TextComponent.of(" ")).append(partComponents);
        }

        return labelComponent;
    }

}
