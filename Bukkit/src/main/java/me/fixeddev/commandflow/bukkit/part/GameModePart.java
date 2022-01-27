package me.fixeddev.commandflow.bukkit.part;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import net.kyori.text.TextComponent;
import net.kyori.text.TranslatableComponent;
import org.bukkit.GameMode;

import java.util.*;

public class GameModePart implements ArgumentPart {

    private static final Map<String, GameMode> GAMEMODE_ALIASES = new HashMap<>();

    static {
        for (GameMode gameMode : GameMode.values()) {
            addAlias(Integer.toString(gameMode.getValue()), gameMode);

            String gameModeName = gameMode.name().toLowerCase();

            addAlias(gameModeName, gameMode);

            if (gameMode == GameMode.SPECTATOR) {
                addAlias("spec", gameMode);
                addAlias("sp", gameMode);

                continue;
            }

            addAlias(Character.toString(gameModeName.charAt(0)), gameMode);
        }
    }

    private final String name;

    public GameModePart(String name) {
        this.name = name;
    }

    public static void addAlias(String alias, GameMode gameMode) {
        GAMEMODE_ALIASES.put(alias, gameMode);
    }

    @Override
    public List<GameMode> parseValue(CommandContext context, ArgumentStack stack, CommandPart parent) throws ArgumentParseException {
        String possibleGameModeName = stack.next().toLowerCase();

        GameMode possibleGameMode = GAMEMODE_ALIASES.get(possibleGameModeName);

        if (possibleGameMode == null) {
            throw new ArgumentParseException(TranslatableComponent.of("invalid.gamemode", TextComponent.of(possibleGameModeName)));
        }

        return Collections.singletonList(possibleGameMode);
    }

    @Override
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        String next = stack.hasNext() ? stack.next() : null;

        if (next == null) {
            return Collections.emptyList();
        }

        String possibleGameModeName = next.toUpperCase();
        List<String> suggestions = new ArrayList<>();

        for (String aliases : GAMEMODE_ALIASES.keySet()) {
            if (possibleGameModeName.isEmpty() || aliases.startsWith(possibleGameModeName)) {
                suggestions.add(aliases);
            }
        }

        return suggestions;
    }

    @Override
    public String getName() {
        return name;
    }

}