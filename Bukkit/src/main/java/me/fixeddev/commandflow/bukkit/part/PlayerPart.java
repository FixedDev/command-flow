package me.fixeddev.commandflow.bukkit.part;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import net.kyori.text.TextComponent;
import net.kyori.text.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static me.fixeddev.commandflow.bukkit.part.OfflinePlayerPart.getStrings;

public class PlayerPart implements ArgumentPart {

    private final String name;
    private final boolean exact;
    private final boolean orSource;

    public PlayerPart(String name, boolean exact, boolean orSource) {
        this.name = name;
        this.exact = exact;
        this.orSource = orSource;
    }

    @Override
    public List<Player> parseValue(CommandContext context, ArgumentStack stack, CommandPart parent) throws ArgumentParseException {
        Player player;

        if (!stack.hasNext()) {
            player = tryGetSender(context);
            if (orSource && player != null) {
                return Collections.singletonList(player);
            }
        }

        String target = stack.next();

        try {
            UUID uuid = UUID.fromString(target);

            player = Bukkit.getPlayer(uuid);
        } catch (IllegalArgumentException ex) {
            if (exact) {
                player = Bukkit.getPlayerExact(target);
            } else {
                player = Bukkit.getPlayer(target);
            }

            if (player == null) {
                player = tryGetSender(context);

                if (orSource && player != null) {
                    return Collections.singletonList(player);
                }

                throw new ArgumentParseException(TranslatableComponent.of("player.offline", TextComponent.of(target)))
                        .setArgument(this);
            }
        }

        if (player == null) {
            player = tryGetSender(context);
            if (orSource && player != null) {
                return Collections.singletonList(player);
            }
        }

        return Collections.singletonList(player);
    }

    private Player tryGetSender(CommandContext context) {
        CommandSender sender = context.getObject(CommandSender.class, BukkitCommandManager.SENDER_NAMESPACE);

        if (sender instanceof Player) {
            return (Player) sender;
        }

        return null;
    }


    @Override
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        return getStrings(stack);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerPart)) return false;
        PlayerPart that = (PlayerPart) o;
        return exact == that.exact &&
                orSource == that.orSource &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, exact, orSource);
    }

}
