package me.fixeddev.commandflow.velocity.factory;

import me.fixeddev.commandflow.annotated.part.PartFactory;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.velocity.part.PlayerSenderPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class PlayerSenderPartFactory implements PartFactory {

    @Override
    public CommandPart createPart(String name, List<? extends Annotation> modifiers) {
        return new PlayerSenderPart(name);
    }
}
