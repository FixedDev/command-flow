package me.fixeddev.commandflow.part.defaults;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.ContextSnapshot;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.exception.CommandException;
import me.fixeddev.commandflow.exception.NoMoreArgumentsException;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.part.PartsWrapper;
import me.fixeddev.commandflow.part.SinglePartWrapper;
import me.fixeddev.commandflow.stack.ArgumentStack;
import me.fixeddev.commandflow.stack.SimpleArgumentStack;
import me.fixeddev.commandflow.stack.StackSnapshot;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OptionalPart implements CommandPart, SinglePartWrapper {
    private final CommandPart part;
    private final List<String> defaultValues;
    private final boolean considerInvalidAsEmpty;

    public OptionalPart(CommandPart part) {
        this(part, true);
    }

    public OptionalPart(CommandPart part, boolean considerInvalidAsEmpty) {
        this(part, considerInvalidAsEmpty, new ArrayList<>());
    }

    public OptionalPart(CommandPart part, List<String> defaultValues) {
        this(part, false, defaultValues);
    }

    public OptionalPart(CommandPart part, boolean considerInvalidAsEmpty, List<String> defaultValues) {
        this.part = part;
        this.defaultValues = defaultValues;
        this.considerInvalidAsEmpty = considerInvalidAsEmpty;

    }

    @Override
    public String getName() {
        return part.getName() + "-optional";
    }

    @Override
    public @Nullable Component getLineRepresentation() {
        Component partLineRepresent = part.getLineRepresentation();

        if (partLineRepresent == null) {
            return null;
        }
        return Component.text()
                .append(Component.text("["))
                .append(partLineRepresent)
                .append(Component.text("]"))
                .build();
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack, CommandPart caller) throws ArgumentParseException {
        StackSnapshot snapshot = stack.getSnapshot();
        ContextSnapshot contextSnapshot = context.getSnapshot();

        try {
            part.parse(context, stack, caller);
        } catch (ArgumentParseException | NoMoreArgumentsException e) {
            if (shouldRewind(caller, e)) {
                throw e;
            }

            stack.applySnapshot(snapshot);
            context.applySnapshot(contextSnapshot);

            if (!defaultValues.isEmpty()) {
                try {
                    part.parse(context, new SimpleArgumentStack(defaultValues), this);
                } catch (ArgumentParseException | NoMoreArgumentsException ignored) {
                }
            }
        }
    }

    private boolean shouldRewind(CommandPart caller, CommandException e) {
        boolean isLast = true;

        if (caller instanceof PartsWrapper) {
            List<CommandPart> parts = ((PartsWrapper) caller).getParts();

            isLast = parts.indexOf(this) == parts.size() - 1;
        }

        return isLast && !(e instanceof NoMoreArgumentsException) && !considerInvalidAsEmpty;
    }

    @Override
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        return Collections.emptyList();
    }

    @Override
    public boolean isAsync() {
        return part.isAsync();
    }

    @Override
    public CommandPart getPart() {
        return part;
    }

    public List<String> getDefaultValues() {
        return defaultValues;
    }

}
