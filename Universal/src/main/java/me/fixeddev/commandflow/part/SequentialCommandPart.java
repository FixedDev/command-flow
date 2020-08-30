package me.fixeddev.commandflow.part;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.stack.ArgumentStack;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class SequentialCommandPart implements CommandPart {

    private String name;
    private List<CommandPart> parts;

    public SequentialCommandPart(String name, List<CommandPart> parts) {
        this.name = name;
        this.parts = parts;
    }

    @Override
    public String getName() {
        return "seq-" + name;
    }

    @Override
    public @Nullable Component getLineRepresentation() {
        TextComponent.Builder builder = TextComponent.builder();
        boolean nonNull = false;

        for (CommandPart part : parts) {
            Component lineRepresentation = part.getLineRepresentation();
            if (lineRepresentation != null) {
                nonNull = true;

                builder.append(lineRepresentation);
            }
        }

        return nonNull ? builder.build() : null;
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack) throws ArgumentParseException {
        for (CommandPart part : parts) {
            part.parse(context, stack);
        }
    }

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
