package team.unnamed.commandflow.part.defaults;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.part.ArgumentPart;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.stack.ArgumentStack;
import team.unnamed.commandflow.stack.SimpleArgumentStack;
import team.unnamed.commandflow.stack.StackSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A {@linkplain CommandPart} that provides direct access to the remaining {@linkplain ArgumentStack}.
 * <p>
 * Actually this shouldn't be an {@linkplain ArgumentPart} since it isn't using the arguments directly, but this is the best way to represent it.
 */
public class ArgumentStackPart implements ArgumentPart {

    private final String name;

    public ArgumentStackPart(String name) {
        this.name = name;
    }

    @Override
    public List<ArgumentStack> parseValue(CommandContext context, ArgumentStack stack, CommandPart parent) throws ArgumentParseException {
        StackSnapshot snapshot = stack.getSnapshot();
        stack.markAsConsumed();

        ArgumentStack newStack = new SimpleArgumentStack(new ArrayList<>());
        newStack.applySnapshot(snapshot, true);

        return Collections.singletonList(newStack);
    }

    @Override
    public String getName() {
        return name;
    }

}
