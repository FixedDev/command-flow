package team.unnamed.commandflow.brigadier.mappings;

import com.mojang.brigadier.tree.CommandNode;
import team.unnamed.commandflow.part.ArgumentPart;

/**
 * Represents the mapping between a command-flow's {@link ArgumentPart} type into a brigadier's {@link CommandNode}.
 * @param <T> The type of the command executor.
 */
public interface CommandNodeMapping<T> {
    /**
     * Converts a specified {@link team.unnamed.commandflow.part.CommandPart} into a {@link CommandNode<T>}.
     * @param argumentPart The {@linkplain ArgumentPart} to convert.
     * @return A non null {@linkplain CommandNode<T>}
     */
    CommandNode<T> convert(ArgumentPart argumentPart);
}
