package me.fixeddev.commandflow.annotated.part;

import me.fixeddev.commandflow.part.CommandPart;

import java.lang.annotation.Annotation;
import java.util.List;

public interface PartFactory {
    /**
     * Creates a new {@link CommandPart} with the given name
     *
     * @param name      The name for the {@link CommandPart}
     * @param modifiers The modifiers for the {@link CommandPart}
     * @return A new {@link CommandPart} with the given name and using the specified modifiers
     */
    CommandPart createPart(String name, List<? extends Annotation> modifiers);
}
