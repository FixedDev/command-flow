package me.fixeddev.commandflow.annotated.part.defaults.modifier;

import me.fixeddev.commandflow.annotated.annotation.ConsumedArgs;
import me.fixeddev.commandflow.annotated.part.PartModifier;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.part.Parts;

import java.lang.annotation.Annotation;
import java.util.List;

public class LimitModifier implements PartModifier {
    @Override
    public CommandPart modify(CommandPart original, List<? extends Annotation> modifiers) {

        ConsumedArgs limit = null;

        for (Annotation modifier : modifiers) {
            if (modifier instanceof ConsumedArgs) {
                limit = (ConsumedArgs) modifier;

                break;
            }
        }

        if (limit == null) {
            return original;
        }

        return Parts.limit(original, limit.value());
    }
}
