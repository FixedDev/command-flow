package team.unnamed.commandflow.bukkit.part;

import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.part.ArgumentPart;
import team.unnamed.commandflow.stack.ArgumentStack;

/**
 * An utility class to ease the use of some {@link CommandPart} specific instances for bukkit.
 */
public final class BukkitParts {

    /**
     * A basic {@link CommandPart} that provides the {@link org.bukkit.command.CommandSender} of the command from
     * the namespace.
     *
     * @param name The name for this part.
     * @return A {@link CommandPart} with a given name that returns the {@link org.bukkit.command.CommandSender} as argument.
     */
    public static CommandSenderPart commandSender(String name) {
        return new CommandSenderPart(name);
    }

    /**
     * A basic {@link ArgumentPart} that takes a string from the {@link ArgumentStack} and parses it into
     * a bukkit's {@link org.bukkit.GameMode}
     *
     * @param name The name for this part.
     * @return A {@link CommandPart} with the given name that takes a {@link String} as argument and returns a Bukkit GameMode.
     */
    public static GameModePart gameMode(String name) {
        return new GameModePart(name);
    }

    /**
     * A basic {@link ArgumentPart} that takes a string from the {@link ArgumentStack} and parses it into
     * a bukkit's {@link org.bukkit.OfflinePlayer}
     *
     * @param name The name for this part.
     * @return A {@link CommandPart} with the given name that takes a {@link String} as argument and returns an OfflinePlayer.
     */
    public static OfflinePlayerPart offlinePlayer(String name) {
        return new OfflinePlayerPart(name);
    }

    /**
     * A {@link ArgumentPart} that takes a string from the {@link ArgumentStack}
     * and tries to parse it into an {@link org.bukkit.OfflinePlayer}, if it is not available or the parsed player isn't
     * valid, then tries to parse the CommandSender as an OfflinePlayer.
     *
     * @param name The name for this part.
     * @return a {@link CommandPart} with the given name that tries to parse an {@link String} as argument or returning the
     * {@link org.bukkit.command.CommandSender} as an {@link org.bukkit.OfflinePlayer}.
     */
    public static OfflinePlayerPart offlinePlayerOrSource(String name) {
        return new OfflinePlayerPart(name, true);
    }

    /**
     * A basic {@link ArgumentPart} that takes a string as nick or uuid from the {@link ArgumentStack} and parses it into
     * a bukkit's {@link org.bukkit.entity.Player} using the method {@link org.bukkit.Bukkit#getPlayer(String)}
     *
     * @param name The name for this part.
     * @return A {@link CommandPart} with the given name that takes a {@link String} as argument and returns an Player.
     */
    public static PlayerPart player(String name) {
        return player(name, false, false);
    }

    /**
     * A basic {@link ArgumentPart} that takes a string as nick from the {@link ArgumentStack} and parses it into
     * a bukkit's {@link org.bukkit.entity.Player} using the method {@link org.bukkit.Bukkit#getPlayerExact(String)}
     *
     * @param name The name for this part.
     * @return A {@link CommandPart} with the given name that takes a {@link String} as argument and returns an Player.
     */
    public static PlayerPart exactPlayer(String name) {
        return player(name, true, false);
    }

    /**
     * A {@link ArgumentPart} that takes a string as nick or as uuid from the {@link ArgumentStack}
     * and tries to parse it into an {@link org.bukkit.entity.Player}, if it is not available or the parsed player isn't
     * valid, then tries to parse the CommandSender as an Player.
     *
     * @param name The name for this part.
     * @return a {@link CommandPart} with the given name that tries to parse an {@link String} as argument or returning the
     * {@link org.bukkit.command.CommandSender} as an {@link org.bukkit.entity.Player}.
     */
    public static PlayerPart playerOrSource(String name) {
        return player(name, false, true);
    }

    /**
     * A {@link ArgumentPart} that takes a string as nick or as uuid (if exact mode is not enabled)
     * from the {@link ArgumentStack} and tries to parse it into an {@link org.bukkit.entity.Player}.
     *
     * @param name The name for this part.
     * @param exact If we should use {@link org.bukkit.Bukkit#getPlayerExact(String)} instead of {@link org.bukkit.Bukkit#getPlayer(String)}
     * @param orSource If we should try to use the CommandSender as Player if the target is not present.
     * @return a {@link CommandPart} with the given name that tries to parse an {@link String} as a Player.
     */
    public static PlayerPart player(String name, boolean exact, boolean orSource) {
        return new PlayerPart(name, exact, orSource);
    }

    /**
     * A {@link CommandPart} that tries parsing the CommandSender as a {@link org.bukkit.entity.Player}.
     *
     * @param name The name for this part.
     * @return a {@link CommandPart} with the given name that tries to parse the command sender as a {@link org.bukkit.entity.Player}.
     */
    public static PlayerSenderPart playerAsSender(String name) {
        return new PlayerSenderPart(name);
    }

    /**
     * A {@link ArgumentPart} that takes a string as world name and tries to parse it into a
     * bukkit's {@link org.bukkit.World}.
     *
     * @param name The name for this part.
     * @return a {@link CommandPart} with the given name that tries to parse an {@link String} as a {@link org.bukkit.World}.
     */
    public static WorldPart world(String name) {
        return new WorldPart(name);
    }

}
