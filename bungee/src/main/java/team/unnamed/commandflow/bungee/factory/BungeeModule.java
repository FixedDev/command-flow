package team.unnamed.commandflow.bungee.factory;

import team.unnamed.commandflow.annotated.part.AbstractModule;
import team.unnamed.commandflow.annotated.part.Key;
import team.unnamed.commandflow.annotated.annotation.Sender;
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
