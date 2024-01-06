package team.unnamed.commandflow.discord.factory;

import team.unnamed.commandflow.annotated.part.PartFactory;
import team.unnamed.commandflow.discord.part.MemberSenderPart;
import team.unnamed.commandflow.part.CommandPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class MemberSenderPartFactory implements PartFactory {

    @Override
    public CommandPart createPart(String name, List<? extends Annotation> modifiers) {
        return new MemberSenderPart(name);
    }
}
