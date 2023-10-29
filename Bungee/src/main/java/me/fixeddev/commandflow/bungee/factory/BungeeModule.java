package me.fixeddev.commandflow.bungee.factory;

import me.fixeddev.commandflow.annotated.annotation.Sender;
import me.fixeddev.commandflow.annotated.part.AbstractModule;
import me.fixeddev.commandflow.annotated.part.Key;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeModule extends AbstractModule {

    @Override
    public void configure() {
        bindFactory(CommandSender.class, new CommandSenderPartFactory());
        bindFactory(ProxiedPlayer.class, new ProxiedPlayerPartFactory());
        bindFactory(new Key(ProxiedPlayer.class, Sender.class), new ProxiedPlayerSenderPartFactory());
    }

}
