package team.unnamed.commandflow.annotated.part.defaults.factory;

import team.unnamed.commandflow.annotated.part.PartFactory;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.part.defaults.ContextPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class ContextFactory implements PartFactory {

    @Override
    public CommandPart createPart(String name, List<? extends Annotation> modifiers) {
        return new ContextPart(name);
    }

}
