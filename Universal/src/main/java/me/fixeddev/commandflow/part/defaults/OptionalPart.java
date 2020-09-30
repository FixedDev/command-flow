package me.fixeddev.commandflow.part.defaults;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.ContextSnapshot;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.exception.NoMoreArgumentsException;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import me.fixeddev.commandflow.stack.SimpleArgumentStack;
import me.fixeddev.commandflow.stack.StackSnapshot;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import org.jetbrains.annotations.Nullable;

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
    public @Nullable Component getLineRepresentation() {
        Component partLineRepresent = part.getLineRepresentation();

        if (partLineRepresent == null) {
            return null;
        }
        return TextComponent.builder("[")
                .append(partLineRepresent)
                .append(TextComponent.of("]")).build();
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack) throws ArgumentParseException {
        StackSnapshot snapshot = stack.getSnapshot();
        ContextSnapshot contextSnapshot = context.getSnapshot();

        try {
            part.parse(context, stack);
        } catch (ArgumentParseException | NoMoreArgumentsException e) {
            stack.applySnapshot(snapshot);
            context.applySnapshot(contextSnapshot);

            if (!defaultValues.isEmpty()) {
                try {
                    part.parse(context, new SimpleArgumentStack(defaultValues));

                    return;
                } catch (ArgumentParseException | NoMoreArgumentsException ignored) {
                }
            }
            /* If this doesn't has any more arguments, then ignore it
            if (!stack.hasNext()) {
                throw e;
            }*/

        }
    }

    @Override
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        return part.getSuggestions(commandContext, stack);
    }

    @Override
    public boolean isAsync() {
        return part.isAsync();
    }
}
