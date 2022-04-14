package me.fixeddev.commandflow.bukkit.part;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class OfflinePlayerPart implements ArgumentPart {

    private final String name;
    private final boolean orSource;

    public OfflinePlayerPart(String name) {
        this(name, false);
    }

    public OfflinePlayerPart(String name, boolean orSource) {
        this.name = name;
        this.orSource = orSource;
    }

    @Override
    public List<? extends OfflinePlayer> parseValue(CommandContext context, ArgumentStack stack, CommandPart parent) throws ArgumentParseException {
        OfflinePlayer player;

        if (!stack.hasNext()) {
            player = tryGetSender(context);
            if (orSource && player != null) {
                return Collections.singletonList(player);
            }
        }

        String target = stack.next();

        try {
            UUID uuid = UUID.fromString(target);

            player = Bukkit.getOfflinePlayer(uuid);
        } catch (IllegalArgumentException ex) {
            player = Bukkit.getOfflinePlayer(target);
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

    static List<String> getStrings(ArgumentStack stack) {
        String last = stack.hasNext() ? stack.next() : null;

        if (last == null) {
            return Collections.emptyList();
        }

        if (Bukkit.getPlayerExact(last) != null) {
            return Collections.emptyList();
        }

        List<String> names = new ArrayList<>();

        for (Player player : Bukkit.matchPlayer(last)) {
            names.add(player.getName());
        }

        return names;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OfflinePlayerPart)) return false;
        OfflinePlayerPart that = (OfflinePlayerPart) o;
        return orSource == that.orSource &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, orSource);
    }

}
