package me.fixeddev.commandflow.bungee.factory;

import me.fixeddev.commandflow.annotated.part.PartFactory;
import me.fixeddev.commandflow.bungee.part.ProxiedPlayerSenderPart;
import me.fixeddev.commandflow.part.CommandPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class ProxiedPlayerSenderPartFactory implements PartFactory {

    @Override
    public CommandPart createPart(String name, List<? extends Annotation> modifiers) {
        return new ProxiedPlayerSenderPart(name);
    }

}
