package me.fixeddev.commandflow.annotated.part.defaults.modifier;

import me.fixeddev.commandflow.annotated.annotation.Flag;
import me.fixeddev.commandflow.annotated.part.PartModifier;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.part.defaults.ValueFlagPart;

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
