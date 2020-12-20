package me.fixeddev.commandflow.annotated.part.defaults.modifier;

import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.annotated.part.PartModifier;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.part.Parts;

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

        return Parts.optional(original, Arrays.asList(optional.value()));
    }
}
