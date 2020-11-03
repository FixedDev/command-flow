package me.fixeddev.commandflow.part.defaults;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.part.SinglePartWrapper;
import me.fixeddev.commandflow.stack.ArgumentStack;
import net.kyori.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LimitingPart implements CommandPart, SinglePartWrapper {

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
    public @Nullable Component getLineRepresentation() {
        return part.getLineRepresentation();
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack) throws ArgumentParseException {
        part.parse(context, stack.getSlice(limit));
    }

    @Override
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        return part.getSuggestions(commandContext, stack.getSlice(limit));
    }

    @Override
    public boolean isAsync() {
        return part.isAsync();
    }

    @Override
    public CommandPart getPart() {
        return part;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommandPart)) return false;
        CommandPart that = (CommandPart) o;
        return that.equals(part);
    }
}
