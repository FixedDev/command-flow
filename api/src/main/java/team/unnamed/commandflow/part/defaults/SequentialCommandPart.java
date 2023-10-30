package team.unnamed.commandflow.part.defaults;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.part.PartsWrapper;
import team.unnamed.commandflow.stack.ArgumentStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SequentialCommandPart implements CommandPart, PartsWrapper {

    private final String name;
    private final List<CommandPart> parts;
    private boolean async;

    public SequentialCommandPart(String name, List<CommandPart> parts) {
        this.name = name;
        this.parts = parts;

        for (CommandPart part : parts) {
            if (part.isAsync()) {
                this.async = true;

                return;
            }
        }
    }

    @Override
    public String getName() {
        return "seq-" + name;
    }

    @Override
    public @Nullable Component getLineRepresentation() {
        TextComponent.Builder builder = Component.text();
        boolean nonNull = false;

        for (CommandPart part : parts) {
            Component lineRepresentation = part.getLineRepresentation();
            if (lineRepresentation != null) {
                if (nonNull) {
                    builder.append(Component.text(" "));
                }
                builder.append(lineRepresentation);

                nonNull = true;

            }
        }

        return nonNull ? builder.build() : null;
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack, CommandPart caller) throws ArgumentParseException {
        for (CommandPart part : parts) {
            part.parse(context, stack, this);
        }
    }

    @Override
    public List<String> getSuggestions(CommandContext context, ArgumentStack stack) {

        for (CommandPart part : parts) {
            List<String> suggestions = part.getSuggestions(context, stack);

            if (suggestions == null) {
                continue;
            }

            if (!stack.hasNext()) {
                return suggestions;
            }
        }

        return Collections.emptyList();
    }

    @Override
    public boolean isAsync() {
        return async;
    }

    @Override
    public List<CommandPart> getParts() {
        return parts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SequentialCommandPart)) return false;
        SequentialCommandPart that = (SequentialCommandPart) o;
        return name.equals(that.name) &&
                parts.equals(that.parts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, parts);
    }

}
