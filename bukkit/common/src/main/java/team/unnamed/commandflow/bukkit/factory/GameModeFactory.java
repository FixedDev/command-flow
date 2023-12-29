package team.unnamed.commandflow.bukkit.factory;

import team.unnamed.commandflow.annotated.part.PartFactory;
import team.unnamed.commandflow.bukkit.part.GameModePart;
import team.unnamed.commandflow.part.CommandPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class GameModeFactory implements PartFactory {

    @Override
    public CommandPart createPart(String name, List<? extends Annotation> modifiers) {
        return new GameModePart(name);
    }

}