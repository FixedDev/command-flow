package me.fixeddev.commandflow.bungee.part;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import net.kyori.text.TextComponent;
import net.kyori.text.TranslatableComponent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ProxiedPlayerPart implements ArgumentPart {

    private final String name;

    public ProxiedPlayerPart(String name) {
        this.name = name;
    }

    @Override
    public List<? extends ProxiedPlayer> parseValue(CommandContext context, ArgumentStack stack) throws ArgumentParseException {
        ProxiedPlayer proxiedPlayer;

        String target = stack.next();

        try {
            UUID uuid = UUID.fromString(target);

            proxiedPlayer = ProxyServer.getInstance().getPlayer(uuid);
        } catch (IllegalArgumentException exception) {
            proxiedPlayer = ProxyServer.getInstance().getPlayer(name);
        }

        if (proxiedPlayer == null) {
            ArgumentParseException exception = new ArgumentParseException(TranslatableComponent.of("player.offline", TextComponent.of(target)));
            exception.setArgument(this);

            throw exception;
        }

        return Collections.singletonList(proxiedPlayer);
    }

    @Override
    public Type getType() {
        return ProxiedPlayer.class;
    }

    @Override
    public String getName() {
        return name;
    }
}
