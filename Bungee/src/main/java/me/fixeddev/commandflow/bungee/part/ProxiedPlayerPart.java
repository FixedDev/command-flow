package me.fixeddev.commandflow.bungee.part;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.bungee.BungeeCommandManager;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import net.kyori.text.TextComponent;
import net.kyori.text.TranslatableComponent;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ProxiedPlayerPart implements ArgumentPart {

    private final String name;
    private final boolean orSource;

    public ProxiedPlayerPart(String name, boolean orSource) {
        this.name = name;
        this.orSource = orSource;
    }

    @Override
    public List<? extends ProxiedPlayer> parseValue(CommandContext context, ArgumentStack stack) throws ArgumentParseException {
        ProxiedPlayer proxiedPlayer;

        if (!stack.hasNext()) {
            proxiedPlayer = tryGetSender(context);
            if (orSource && proxiedPlayer != null) {
                return Collections.singletonList(proxiedPlayer);
            }
        }

        String target = stack.next();

        try {
            UUID uuid = UUID.fromString(target);

            proxiedPlayer = ProxyServer.getInstance().getPlayer(uuid);
        } catch (IllegalArgumentException exception) {
            proxiedPlayer = ProxyServer.getInstance().getPlayer(name);

            if (proxiedPlayer == null) {
                ArgumentParseException argumentParseException = new ArgumentParseException(TranslatableComponent.of("player.offline", TextComponent.of(target)));
                argumentParseException.setArgument(this);

                throw exception;
            }
        }

        if (proxiedPlayer == null) {
            proxiedPlayer = tryGetSender(context);
            if (orSource && proxiedPlayer != null) {
                return Collections.singletonList(proxiedPlayer);
            }
        }

        return Collections.singletonList(proxiedPlayer);
    }

    private ProxiedPlayer tryGetSender(CommandContext context) {
        CommandSender sender = context.getObject(CommandSender.class, BungeeCommandManager.SENDER_NAMESPACE);

        if (sender instanceof ProxiedPlayer) {
            return (ProxiedPlayer) sender;
        }

        return null;
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
