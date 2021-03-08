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

/**
 * A {@linkplain me.fixeddev.commandflow.part.CommandPart} that provides direct access to the remaining {@linkplain ArgumentStack}.
 * <p>
 * Actually this shouldn't be an {@linkplain ArgumentPart} since it isn't using the arguments directly, but this is the best way to represent it.
 */
public class ArgumentStackPart implements ArgumentPart {

    private final String name;

    public ArgumentStackPart(String name) {
        this.name = name;
    }

    @Override
    public List<ArgumentStack> parseValue(CommandContext context, ArgumentStack stack) throws ArgumentParseException {
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
        return name;
    }
}
