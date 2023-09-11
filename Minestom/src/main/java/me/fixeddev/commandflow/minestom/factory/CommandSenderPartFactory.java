package me.fixeddev.commandflow.minestom.factory;

import me.fixeddev.commandflow.annotated.part.PartFactory;
import me.fixeddev.commandflow.minestom.part.CommandSenderPart;
import me.fixeddev.commandflow.part.CommandPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class CommandSenderPartFactory implements PartFactory {

    private final boolean expectPlayer;

    public CommandSenderPartFactory(boolean expectPlayer) {
        this.expectPlayer = expectPlayer;
    }

    @Override
    public CommandPart createPart(String name, List<? extends Annotation> modifiers) {
        return new CommandSenderPart(name, expectPlayer);
    }
}