package team.unnamed.commandflow.annotated.part.defaults.modifier;

import team.unnamed.commandflow.annotated.annotation.Flag;
import team.unnamed.commandflow.annotated.part.PartModifier;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.part.defaults.ValueFlagPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class ValueFlagModifier implements PartModifier {

    @Override
    public CommandPart modify(CommandPart original, List<? extends Annotation> modifiers) {
        Flag flag = getModifier(modifiers, Flag.class);

        String shortName = flag != null ? flag.value() + "" : original.getName();

        return new ValueFlagPart(shortName, flag != null && flag.allowFullName(), original);
    }

}
