package team.unnamed.commandflow.bukkit.factory;

import team.unnamed.commandflow.annotated.part.PartFactory;
import team.unnamed.commandflow.bukkit.part.CommandSenderPart;
import team.unnamed.commandflow.part.CommandPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class CommandSenderFactory implements PartFactory {

    @Override
    public CommandPart createPart(String name, List<? extends Annotation> modifiers) {
        return new CommandSenderPart(name);
    }

}
