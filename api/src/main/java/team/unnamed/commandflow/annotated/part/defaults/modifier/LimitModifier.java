package team.unnamed.commandflow.annotated.part.defaults.modifier;

import team.unnamed.commandflow.annotated.annotation.Limit;
import team.unnamed.commandflow.annotated.part.PartModifier;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.part.Parts;

import java.lang.annotation.Annotation;
import java.util.List;

public class LimitModifier implements PartModifier {

    @Override
    public CommandPart modify(CommandPart original, List<? extends Annotation> modifiers) {
        Limit limit = getModifier(modifiers, Limit.class);

        if (limit == null) {
            return original;
        }

        return Parts.limit(original, limit.value());
    }

}
