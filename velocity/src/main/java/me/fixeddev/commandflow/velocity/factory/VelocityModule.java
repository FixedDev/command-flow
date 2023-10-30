package me.fixeddev.commandflow.velocity.factory;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import me.fixeddev.commandflow.annotated.part.AbstractModule;
import me.fixeddev.commandflow.annotated.part.Key;
import me.fixeddev.commandflow.velocity.annotation.Sender;

public class VelocityModule extends AbstractModule {

    private final ProxyServer proxyServer;

    public VelocityModule(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }

    @Override
    public void configure() {
        bindFactory(CommandSource.class, new CommandSourcePartFactory());
        bindFactory(Player.class, new PlayerPartFactory(proxyServer));
        bindFactory(new Key(Player.class, Sender.class), new PlayerSenderPartFactory());
    }
}
