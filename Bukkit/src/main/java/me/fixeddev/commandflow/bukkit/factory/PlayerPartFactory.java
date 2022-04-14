package me.fixeddev.commandflow.bukkit.factory;

import me.fixeddev.commandflow.annotated.part.PartFactory;
import me.fixeddev.commandflow.bukkit.annotation.Exact;
import me.fixeddev.commandflow.bukkit.annotation.PlayerOrSource;
import me.fixeddev.commandflow.bukkit.part.PlayerPart;
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
