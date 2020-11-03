package me.fixeddev.commandflow.part;

/**
 * An interface to denote the {@link CommandPart} types that are wrappers for other parts,
 * this is used specifically for a {@link CommandPart} that wraps only one part.
 */
public interface SinglePartWrapper extends CommandPart{
    /**
     * The {@link CommandPart}  that is wrapped by this wrapper.
     *
     * @return The {@link CommandPart} that is wrapped by this wrapper.
     */
    CommandPart getPart();
}
