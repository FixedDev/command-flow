package me.fixeddev.commandflow.bukkit.factory;

import me.fixeddev.commandflow.annotated.part.PartFactory;
import me.fixeddev.commandflow.bukkit.part.CommandSenderPart;
import me.fixeddev.commandflow.part.CommandPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class CommandSenderFactory implements PartFactory {

    @Override
    public CommandPart createPart(String name, List<? extends Annotation> modifiers) {
        return new CommandSenderPart(name);
    }

}
