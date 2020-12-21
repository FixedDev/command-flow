package me.fixeddev.commandflow.annotated.part.defaults.factory;

import me.fixeddev.commandflow.annotated.annotation.Range;
import me.fixeddev.commandflow.annotated.part.PartFactory;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.part.defaults.IntegerPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class IntegerPartFactory implements PartFactory {
    @Override
    public CommandPart createPart(String name, List<? extends Annotation> modifiers) {
        Range range = getAnnotation(modifiers, Range.class);

        if (range != null) {
            return new IntegerPart(name, range.min(), range.max());
        }

        return new IntegerPart(name);
    }
}
