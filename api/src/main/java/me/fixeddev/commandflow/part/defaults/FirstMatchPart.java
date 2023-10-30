package me.fixeddev.commandflow.part.defaults;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.ContextSnapshot;
import me.fixeddev.commandflow.exception.ArgumentException;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.part.PartsWrapper;
import me.fixeddev.commandflow.stack.ArgumentStack;
import me.fixeddev.commandflow.stack.StackSnapshot;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * A {@linkplain CommandPart} that wraps multiple {@linkplain CommandPart}s, allowing for multiple parsing paths.
 * <p>
 * The first part to parse is the one being used to parse.
 */
public class FirstMatchPart implements CommandPart, PartsWrapper {

    private final String name;
    private final List<CommandPart> partList;
    private Boolean async;
    private final boolean considerNoChangesAsFail;

    /**
     * Creates a new FirstMatchPart with a specified name, and a specified list of parts to parse, also allowing to change
     * the "considerNoChangesAsFail" flag.
     *
     * @param name                    The name for this FirstMatchPart.
     * @param partList                The list of parts that will be parsed.
     * @param considerNoChangesAsFail Whether to consider as fail when no changes in the {@linkplain CommandContext} or in the {@linkplain ArgumentStack}
     *                                as a fail.
     */
    public FirstMatchPart(String name, List<CommandPart> partList, boolean considerNoChangesAsFail) {
        this.name = name;
        this.partList = partList;
        this.considerNoChangesAsFail = considerNoChangesAsFail;
    }

    public FirstMatchPart(String name, List<CommandPart> partList) {
        this(name, partList, true);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public @Nullable Component getLineRepresentation() {
        TextComponent component = Component.text("<");

        boolean first = true;
        for (CommandPart part : partList) {
            Component partComponent = part.getLineRepresentation();

            if (partComponent != null) {
                if (first) {
                    first = false;
                } else {
                    component = component.append(Component.text("|"));
                }

                component = component.append(partComponent);

            }
        }
        component = component.append(Component.text(">"));

        return component;
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack, CommandPart caller) throws ArgumentParseException {
        ArgumentException last = null;

        Iterator<CommandPart> partIterator = partList.iterator();

        while (partIterator.hasNext()) {
            CommandPart part = partIterator.next();

            ContextSnapshot contextSnapshot = context.getSnapshot();
            StackSnapshot snapshot = stack.getSnapshot();

            try {
                part.parse(context, stack, this);

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
            ContextSnapshot snapshot = context.getSnapshot();
            StackSnapshot stackSnapshot = stack.getSnapshot();

            List<String> partSuggestions = part.getSuggestions(context, stack);

            if (partSuggestions != null) {
                suggestions.addAll(partSuggestions);
            }

            stack.applySnapshot(stackSnapshot, true);
            context.applySnapshot(snapshot);
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
