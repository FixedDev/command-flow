package team.unnamed.commandflow.annotated.part.defaults.factory;

import team.unnamed.commandflow.annotated.annotation.ConsumeAll;
import team.unnamed.commandflow.annotated.part.PartFactory;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.part.defaults.StringPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class StringPartFactory implements PartFactory {

    @Override
    public CommandPart createPart(String name, List<? extends Annotation> modifiers) {
        return new StringPart(name, getAnnotation(modifiers, ConsumeAll.class) != null);
    }

}
