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
