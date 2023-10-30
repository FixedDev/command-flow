package me.fixeddev.commandflow.annotated.part.defaults.factory;

import me.fixeddev.commandflow.annotated.part.PartFactory;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.part.Parts;

import java.lang.annotation.Annotation;
import java.util.List;

public class EnumPartFactory implements PartFactory {

    private final Class<? extends Enum<?>> enumType;

    public EnumPartFactory(Class<? extends Enum<?>> enumType) {
        if (enumType == null) {
            throw new IllegalArgumentException("The enum type can't be null!");
        }

        this.enumType = enumType;
    }

    @Override
    public CommandPart createPart(String name, List<? extends Annotation> modifiers) {
        return Parts.enumPart(name, enumType);
    }

}
