package me.fixeddev.commandflow.minestom;

import me.fixeddev.commandflow.annotated.part.AbstractModule;
import me.fixeddev.commandflow.annotated.part.Key;
import me.fixeddev.commandflow.minestom.annotation.Sender;
import me.fixeddev.commandflow.minestom.factory.CommandSenderPartFactory;
import me.fixeddev.commandflow.minestom.factory.PlayerPartFactory;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;

public class MinestomModule extends AbstractModule {

    @Override
    public void configure() {
        bindFactory(new Key(Player.class), new PlayerPartFactory());
        bindFactory(new Key(CommandSender.class, Sender.class), new CommandSenderPartFactory(false));
        bindFactory(new Key(Player.class, Sender.class), new CommandSenderPartFactory(true));
    }
}