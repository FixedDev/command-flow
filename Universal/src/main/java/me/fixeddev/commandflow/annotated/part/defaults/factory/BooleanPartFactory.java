package me.fixeddev.commandflow.annotated.part.defaults.factory;

import me.fixeddev.commandflow.annotated.part.PartFactory;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.part.defaults.BooleanPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class BooleanPartFactory implements PartFactory {
    @Override
    public CommandPart createPart(String name, List<? extends Annotation> modifiers) {
        return new BooleanPart(name);
    }
}
