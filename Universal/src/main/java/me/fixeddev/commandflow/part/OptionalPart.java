package me.fixeddev.commandflow.part;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.exception.NoMoreArgumentsException;
import me.fixeddev.commandflow.stack.ArgumentStack;
import me.fixeddev.commandflow.stack.SimpleArgumentStack;
import me.fixeddev.commandflow.stack.StackSnapshot;

import java.util.ArrayList;
import java.util.List;

public class OptionalPart implements CommandPart {

    private final CommandPart part;
    private final List<String> defaultValues;

    public OptionalPart(CommandPart part) {
        this.part = part;
        this.defaultValues = new ArrayList<>();
    }

    public OptionalPart(CommandPart part, List<String> defaultValues) {
        this.part = part;
        this.defaultValues = defaultValues;
    }

    @Override
    public String getName() {
        return part.getName() + "-optional";
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack) throws ArgumentParseException {
        StackSnapshot snapshot = stack.getSnapshot();

        try {
            part.parse(context, stack);
        } catch (ArgumentParseException | NoMoreArgumentsException e) {
            stack.applySnapshot(snapshot);

            if (!defaultValues.isEmpty()) {
                try {
                    part.parse(context, new SimpleArgumentStack(defaultValues));

                    return;
                } catch (ArgumentParseException | NoMoreArgumentsException ignored) {}
            }
            if (!stack.hasNext()) {
                throw e;
            }

        }
    }
}
