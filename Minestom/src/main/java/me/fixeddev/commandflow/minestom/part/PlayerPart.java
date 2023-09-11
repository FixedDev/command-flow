package me.fixeddev.commandflow.minestom.part;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.minestom.MinestomCommandManager;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import net.minestom.server.network.ConnectionManager;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerPart implements CommandPart {

    private final String name;
    private final boolean exact;
    private final boolean orSource;

    public PlayerPart(String name, boolean exact, boolean orSource) {
        this.name = name;
        this.exact = exact;
        this.orSource = orSource;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack, @Nullable CommandPart caller) throws ArgumentParseException {
        String next = stack.hasNext() ? stack.next() : null;

        if (next == null) return;

        Player player = null;

        ConnectionManager connectionManager = MinecraftServer.getConnectionManager();
        try {
            UUID uuid = UUID.fromString(next);

            player = connectionManager.getPlayer(uuid);
        } catch (Exception e) {
            if (exact) {
                List<Player> filtered = connectionManager.getOnlinePlayers()
                        .stream()
                        .filter(element -> element.getUsername().equals(next))
                        .collect(Collectors.toList());

                if (!filtered.isEmpty()) {
                    player = filtered.get(0);
                }
            } else {
                player = connectionManager.findPlayer(next.toLowerCase(Locale.ROOT));
            }
        }

        if (player == null && orSource) {
            player = senderAsPlayer(context);
        }

        if (player == null) {
            throw new ArgumentParseException(Component.translatable("player.offline", Component.text(next)))
                    .setArgument(this);
        }

        context.setValue(this, player);
    }

    private Player senderAsPlayer(CommandContext context) {
        CommandSender sender = context.getObject(CommandSender.class, MinestomCommandManager.SENDER_NAMESPACE);

        if (sender instanceof Player) {
            return (Player) sender;
        }

        return null;
    }

    @Override
    public @Nullable List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        String next = stack.hasNext() ? stack.next() : null;

        if (next == null) return Collections.emptyList();

        List<String> matchedPlayers = new ArrayList<>();

        for (Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            String playerName = player.getUsername();

            if (next.equalsIgnoreCase(playerName)) {
                matchedPlayers.clear();
                matchedPlayers.add(playerName);
                break;
            }

            if (playerName.toLowerCase(java.util.Locale.ENGLISH).contains(next.toLowerCase(java.util.Locale.ENGLISH))) {
                matchedPlayers.add(playerName);
            }
        }

        return matchedPlayers;
    }
}