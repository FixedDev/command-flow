package team.unnamed.commandflow.part;

import team.unnamed.commandflow.part.visitor.CommandPartVisitor;

import java.util.List;

/**
 * An interface to denote the {@link CommandPart} types that are wrappers for other parts,
 * this is used specifically for a {@link CommandPart} that wraps multiple parts.
 */
public interface PartsWrapper extends CommandPart {

    /**
     * The {@link CommandPart} instances that are wrapped by this wrapper.
     *
     * @return The {@link CommandPart} instances that are wrapped by this wrapper.
     */
    List<CommandPart> getParts();

    @Override
    default <T> T acceptVisitor(CommandPartVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
