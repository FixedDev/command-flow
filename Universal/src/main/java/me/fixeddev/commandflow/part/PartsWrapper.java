package me.fixeddev.commandflow.part;

import java.util.List;

/**
 * An interface to denote the {@link CommandPart} types that are wrappers for other parts,
 * this is used specifically for a {@link CommandPart} that wraps multiple parts.
 */
public interface PartsWrapper extends CommandPart{
    /**
     * The {@link CommandPart} instances that are wrapped by this wrapper.
     *
     * @return The {@link CommandPart} instances that are wrapped by this wrapper.
     */
    List<CommandPart> getParts();
}
