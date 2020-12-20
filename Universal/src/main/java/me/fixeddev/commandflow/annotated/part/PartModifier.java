package me.fixeddev.commandflow.annotated.part;

import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.part.CommandPart;

import java.lang.annotation.Annotation;
import java.util.List;

public interface PartModifier {
    /**
     * Modifies the given {@link CommandPart} wrapping or changing values of it
     *
     * @param original  The original {@link CommandPart} to modify.
     * @param modifiers The modifiers for the {@link CommandPart}
     * @return The modified version of {@link CommandPart}.
     */
    CommandPart modify(CommandPart original, List<? extends Annotation> modifiers);

    default <T extends Annotation> T getModifier(List<? extends Annotation> modifiers, Class<? extends T> type) {
        T finalModifier = null;

        for (Annotation modifier : modifiers) {
            if (modifier.annotationType() == type) {
                finalModifier = (T) modifier;

                break;
            }
        }

        return finalModifier;
    }
}
