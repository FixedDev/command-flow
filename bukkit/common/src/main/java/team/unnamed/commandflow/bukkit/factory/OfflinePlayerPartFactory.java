package team.unnamed.commandflow.bukkit.factory;

import team.unnamed.commandflow.annotated.part.PartFactory;
import team.unnamed.commandflow.bukkit.annotation.PlayerOrSource;
import team.unnamed.commandflow.bukkit.part.OfflinePlayerPart;
import team.unnamed.commandflow.part.CommandPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class OfflinePlayerPartFactory implements PartFactory {

    @Override
    public CommandPart createPart(String name, List<? extends Annotation> modifiers) {
        boolean orSource = getAnnotation(modifiers, PlayerOrSource.class) != null;

        return new OfflinePlayerPart(name, orSource);
    }

}
