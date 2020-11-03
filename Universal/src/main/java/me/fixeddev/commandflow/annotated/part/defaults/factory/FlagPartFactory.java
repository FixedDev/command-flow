package me.fixeddev.commandflow.annotated.part.defaults.factory;

import me.fixeddev.commandflow.annotated.annotation.Flag;
import me.fixeddev.commandflow.annotated.part.PartFactory;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.part.defaults.FlagPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class FlagPartFactory implements PartFactory {

    @Override
    public CommandPart createPart(String name, List<? extends Annotation> modifiers) {
        Flag flag = null;

        for (Annotation modifier : modifiers) {
            if (modifier instanceof Flag) {
                flag = (Flag) modifier;
            }
        }

        String shortName = flag != null ? flag.value() + "" : name;

        return new FlagPart(name, shortName, flag != null && flag.allowFullName());
    }
}
