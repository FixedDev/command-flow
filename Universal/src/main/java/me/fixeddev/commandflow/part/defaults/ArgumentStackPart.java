package me.fixeddev.commandflow.part.defaults;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import me.fixeddev.commandflow.stack.SimpleArgumentStack;
import me.fixeddev.commandflow.stack.StackSnapshot;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArgumentStackPart implements ArgumentPart {

    private String name;

    public ArgumentStackPart(String name) {
        this.name = name;
    }

    @Override
    public List<? extends Object> parseValue(CommandContext context, ArgumentStack stack) throws ArgumentParseException {
        StackSnapshot snapshot = stack.getSnapshot();
        stack.markAsConsumed();

        ArgumentStack newStack = new SimpleArgumentStack(new ArrayList<>());
        newStack.applySnapshot(snapshot, true);

        return Collections.singletonList(newStack);
    }

    @Override
    public Type getType() {
        return ArgumentStack.class;
    }

    @Override
    public String getName() {
        return null;
    }
}
