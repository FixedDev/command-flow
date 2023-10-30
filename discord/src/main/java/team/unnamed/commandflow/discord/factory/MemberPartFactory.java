package team.unnamed.commandflow.discord.factory;

import team.unnamed.commandflow.annotated.part.PartFactory;
import team.unnamed.commandflow.discord.part.MemberPart;
import team.unnamed.commandflow.part.CommandPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class MemberPartFactory implements PartFactory {

    @Override
    public CommandPart createPart(String name, List<? extends Annotation> modifiers) {
        return new MemberPart(name);
    }
}
