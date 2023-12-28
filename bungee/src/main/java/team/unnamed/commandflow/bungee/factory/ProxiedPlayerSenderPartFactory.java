package team.unnamed.commandflow.bungee.factory;

import team.unnamed.commandflow.annotated.part.PartFactory;
import team.unnamed.commandflow.bungee.part.ProxiedPlayerSenderPart;
import team.unnamed.commandflow.part.CommandPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class ProxiedPlayerSenderPartFactory implements PartFactory {

    @Override
    public CommandPart createPart(String name, List<? extends Annotation> modifiers) {
        return new ProxiedPlayerSenderPart(name);
    }

}
