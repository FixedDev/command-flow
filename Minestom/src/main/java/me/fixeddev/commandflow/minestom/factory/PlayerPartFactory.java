package me.fixeddev.commandflow.minestom.factory;

import me.fixeddev.commandflow.annotated.part.PartFactory;
import me.fixeddev.commandflow.minestom.annotation.Exact;
import me.fixeddev.commandflow.minestom.annotation.PlayerOrSource;
import me.fixeddev.commandflow.minestom.part.PlayerPart;
import me.fixeddev.commandflow.part.CommandPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class PlayerPartFactory implements PartFactory {
    @Override
    public CommandPart createPart(String name, List<? extends Annotation> modifiers) {
        boolean orSource = getAnnotation(modifiers, PlayerOrSource.class) != null;
        boolean exact = getAnnotation(modifiers, Exact.class) != null;

        return new PlayerPart(name, exact, orSource);
    }
}