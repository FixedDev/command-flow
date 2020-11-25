package me.fixeddev.commandflow.part.defaults;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.ContextSnapshot;
import me.fixeddev.commandflow.exception.ArgumentException;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.part.PartsWrapper;
import me.fixeddev.commandflow.stack.ArgumentStack;
import me.fixeddev.commandflow.stack.StackSnapshot;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class FirstMatchPart implements CommandPart, PartsWrapper {

    private final String name;
    private final List<CommandPart> partList;
    private Boolean async;
    private boolean considerNoChangesAsFail;

    public FirstMatchPart(String name, List<CommandPart> partList, boolean considerNoChangesAsFail) {
        this.name = name;
        this.partList = partList;
        this.considerNoChangesAsFail = considerNoChangesAsFail;
    }

    public FirstMatchPart(String name, List<CommandPart> partList) {
        this(name, partList, false);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public @Nullable Component getLineRepresentation() {
        TextComponent component = TextComponent.of("<");

        boolean first = true;
        for (CommandPart part : partList) {
            Component partComponent = part.getLineRepresentation();

            if (partComponent != null) {
                if (first) {
                    first = false;
                } else {
                    component = component.append(TextComponent.of("|"));
                }

                component = component.append(partComponent);

            }
        }
        component = component.append(TextComponent.of(">"));

        return component;
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack) throws ArgumentParseException {
        ArgumentException last = null;

        Iterator<CommandPart> partIterator = partList.iterator();

        while (partIterator.hasNext()) {
            CommandPart part = partIterator.next();

            ContextSnapshot contextSnapshot = context.getSnapshot();
            StackSnapshot snapshot = stack.getSnapshot();

            try {
                part.parse(context, stack);

                // make it a toggleable behaviour, since it can be kinda buggy
                if (!considerNoChangesAsFail) {
                    return;
                }

                ContextSnapshot newState = context.getSnapshot();
                StackSnapshot newStackState = stack.getSnapshot();

                // Check if there was any change to the context or the stack
                if (newState.equals(contextSnapshot) &&
                        newStackState.equals(snapshot) &&
                        !partIterator.hasNext()) {

                    // The part actually failed but the exception was swallowed at some point of the stack.
                    if (last == null) {
                        last = new ArgumentParseException("");
                    }

                    throw last;
                }

                return;
            } catch (ArgumentException e) {
                if (partIterator.hasNext()) {
                    stack.applySnapshot(snapshot, true);
                    context.applySnapshot(contextSnapshot);
                }

                last = e;
            }
        }
        if (last != null) {
            throw last;
        }
    }

    @Override
    public List<String> getSuggestions(CommandContext context, ArgumentStack stack) {
        List<String> suggestions = new ArrayList<>();

        for (CommandPart part : partList) {
            suggestions.addAll(part.getSuggestions(context, stack));
        }

        return suggestions;
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
