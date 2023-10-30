package me.fixeddev.commandflow.velocity.factory;

import com.velocitypowered.api.proxy.ProxyServer;
import me.fixeddev.commandflow.annotated.part.PartFactory;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.velocity.annotation.PlayerOrSource;
import me.fixeddev.commandflow.velocity.part.PlayerPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class PlayerPartFactory implements PartFactory {

    private final ProxyServer proxyServer;

    public PlayerPartFactory(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }

    @Override
    public CommandPart createPart(String name, List<? extends Annotation> modifiers) {
        boolean orSource = getAnnotation(modifiers, PlayerOrSource.class) != null;

        return new PlayerPart(proxyServer, name, orSource);
    }
}
