package me.fixeddev.commandflow.part;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.stack.ArgumentStack;

public class LimitingPart implements CommandPart {

    private final String name;
    private final int limit;
    private final CommandPart part;

    public LimitingPart(CommandPart part, int limit) {
        name = part.getName() + "-limiting";
        this.limit = limit;
        this.part = part;

        if (limit <= 0) {
            throw new IllegalArgumentException("The limit must be a positive number that's more than 0!");
        }

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack) throws ArgumentParseException {
        part.parse(context, stack.getSlice(limit));
    }
}
