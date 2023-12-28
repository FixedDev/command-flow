package team.unnamed.commandflow.annotated.part;

import team.unnamed.commandflow.part.CommandPart;

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
