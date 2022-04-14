package me.fixeddev.commandflow.bungee.factory;

import me.fixeddev.commandflow.annotated.part.PartFactory;
import me.fixeddev.commandflow.bungee.annotation.ProxiedPlayerOrSource;
import me.fixeddev.commandflow.bungee.part.ProxiedPlayerPart;
import me.fixeddev.commandflow.part.CommandPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class ProxiedPlayerPartFactory implements PartFactory {

    @Override
    public CommandPart createPart(String name, List<? extends Annotation> modifiers) {
        boolean orSource = false;

        for (Annotation modifier : modifiers) {
            if (modifier.annotationType() == ProxiedPlayerOrSource.class) {
                orSource = true;
            }
        }

        return new ProxiedPlayerPart(name, orSource);
    }

}
