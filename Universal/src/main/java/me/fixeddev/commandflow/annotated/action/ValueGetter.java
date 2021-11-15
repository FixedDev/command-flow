package me.fixeddev.commandflow.annotated.action;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.part.CommandPart;

import java.util.Collections;

public interface ValueGetter {
    Object getValue(CommandContext commandContext);

    static ValueGetter forPart(CommandPart part) {
        return commandContext ->
                commandContext.getValue(part).orElse(null);
    }

    static ValueGetter forPart(CommandPart part, int index) {
        return commandContext ->
                commandContext.getPart(part.getName(), index)
                        .flatMap(commandContext::getValue).orElse(null);
    }

    static ValueGetter forOptionalPart(CommandPart part) {
        return commandContext ->
                commandContext.getValue(part);
    }

    static ValueGetter forOptionalPart(CommandPart part, int index) {
        return commandContext ->
                commandContext.getPart(part.getName(), index)
                        .flatMap(commandContext::getValue);
    }

    static ValueGetter forPartValues(CommandPart part) {
        return commandContext ->
                commandContext.getValues(part);
    }

    static ValueGetter forPartValues(CommandPart part, int index) {
        return commandContext ->
                commandContext.getPart(part.getName(), index)
                        .map(commandContext::getValues);
    }

    static ValueGetter forPartRaw(CommandPart part) {
        return commandContext ->
                commandContext.getRaw(part);
    }

    static ValueGetter ofInstance(Object object){
        return commandContext -> object;
    }
}
