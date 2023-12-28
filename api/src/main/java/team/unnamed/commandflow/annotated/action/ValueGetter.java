package team.unnamed.commandflow.annotated.action;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.part.CommandPart;

public interface ValueGetter {
    Object getValue(CommandContext commandContext);

    static ValueGetter forPart(CommandPart part) {
        return commandContext ->
                commandContext.getValue(part).orElse(null);
    }

    static ValueGetter forPart(String partName, int index) {
        return commandContext ->
                commandContext.getPart(partName, index)
                        .flatMap(commandContext::getValue).orElse(null);
    }

    static ValueGetter forOptionalPart(CommandPart part) {
        return commandContext ->
                commandContext.getValue(part);
    }

    static ValueGetter forOptionalPart(String partName, int index) {
        return commandContext ->
                commandContext.getPart(partName, index)
                        .flatMap(commandContext::getValue);
    }

    static ValueGetter forPartValues(CommandPart part) {
        return commandContext ->
                commandContext.getValues(part);
    }

    static ValueGetter forPartValues(String partName, int index) {
        return commandContext ->
                commandContext.getPart(partName, index)
                        .map(commandContext::getValues);
    }

    static ValueGetter forPartRaw(CommandPart part) {
        return commandContext ->
                commandContext.getRaw(part);
    }

    static ValueGetter ofInstance(Object object) {
        return commandContext -> object;
    }
}
