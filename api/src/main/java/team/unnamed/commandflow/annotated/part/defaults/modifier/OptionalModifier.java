package team.unnamed.commandflow.annotated.part.defaults.modifier;

import team.unnamed.commandflow.annotated.annotation.OptArg;
import team.unnamed.commandflow.annotated.annotation.Strict;
import team.unnamed.commandflow.annotated.part.PartModifier;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.part.Parts;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

public class OptionalModifier implements PartModifier {
    @Override
    public CommandPart modify(CommandPart original, List<? extends Annotation> modifiers) {
        OptArg optional = getModifier(modifiers, OptArg.class);

        if (optional == null) {
            return original;
        }

        if (getModifier(modifiers, Strict.class) != null) {
            return Parts.strictOptional(original, Arrays.asList(optional.value()));
        }

        return Parts.optional(original,Arrays.asList(optional.value()));
    }
}
