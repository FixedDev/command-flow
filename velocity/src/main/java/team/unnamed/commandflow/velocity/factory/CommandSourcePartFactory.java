package team.unnamed.commandflow.velocity.factory;

import team.unnamed.commandflow.annotated.part.PartFactory;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.velocity.part.CommandSenderPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class CommandSourcePartFactory implements PartFactory {

    @Override
    public CommandPart createPart(String name, List<? extends Annotation> modifiers) {
        return new CommandSenderPart(name);
    }
}
