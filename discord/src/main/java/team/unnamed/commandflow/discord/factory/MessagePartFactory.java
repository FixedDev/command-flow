package team.unnamed.commandflow.discord.factory;

import team.unnamed.commandflow.annotated.part.PartFactory;
import team.unnamed.commandflow.discord.part.MessagePart;
import team.unnamed.commandflow.part.CommandPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class MessagePartFactory implements PartFactory {

    @Override
    public CommandPart createPart(String name, List<? extends Annotation> modifiers) {
        return new MessagePart(name);
    }
}
