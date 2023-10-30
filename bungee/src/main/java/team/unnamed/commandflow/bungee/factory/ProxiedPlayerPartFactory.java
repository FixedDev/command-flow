package team.unnamed.commandflow.bungee.factory;

import team.unnamed.commandflow.annotated.part.PartFactory;
import team.unnamed.commandflow.bungee.annotation.ProxiedPlayerOrSource;
import team.unnamed.commandflow.bungee.part.ProxiedPlayerPart;
import team.unnamed.commandflow.part.CommandPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class ProxiedPlayerPartFactory implements PartFactory {

    @Override
    public CommandPart createPart(String name, List<? extends Annotation> modifiers) {
        boolean orSource = getAnnotation(modifiers, ProxiedPlayerOrSource.class) != null;

        return new ProxiedPlayerPart(name, orSource);
    }

}
