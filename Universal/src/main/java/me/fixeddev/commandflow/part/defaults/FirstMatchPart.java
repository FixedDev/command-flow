package me.fixeddev.commandflow.part.defaults;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.ContextSnapshot;
import me.fixeddev.commandflow.exception.ArgumentException;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.part.PartsWrapper;
import me.fixeddev.commandflow.stack.ArgumentStack;
import me.fixeddev.commandflow.stack.StackSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FirstMatchPart implements CommandPart, PartsWrapper {

    private final String name;
    private final List<CommandPart> partList;
    private Boolean async;

    public FirstMatchPart(String name, List<CommandPart> partList) {
        this.name = name;
        this.partList = partList;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack) throws ArgumentParseException {
        ArgumentException last = null;

        for (CommandPart part : partList) {
            ContextSnapshot contextSnapshot = context.getSnapshot();
            StackSnapshot snapshot = stack.getSnapshot();

            try {
                part.parse(context, stack);

                return;
            } catch (ArgumentException e) {
                stack.applySnapshot(snapshot, true);
                context.applySnapshot(contextSnapshot);

                last = e;
            }
        }

        if(last != null) {
            throw last;
        }
    }

    @Override
    public List<String> getSuggestions(CommandContext context, ArgumentStack stack) {
        List<String> suggestions = new ArrayList<>();

        for (CommandPart part : partList) {
            suggestions.addAll(part.getSuggestions(context, stack));

            if (!suggestions.isEmpty()) {
                return suggestions;
            }
        }

        return Collections.emptyList();
    }

    @Override
    public boolean isAsync() {
        if (async == null) {
            for (CommandPart part : partList) {
                if (part.isAsync()) {
                    return async = true;
                }
            }

            return async = false;
        }

        return async;
    }

    @Override
    public List<CommandPart> getParts() {
        return partList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FirstMatchPart)) return false;
        FirstMatchPart that = (FirstMatchPart) o;
        return name.equals(that.name) &&
                partList.equals(that.partList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, partList);
    }
}
