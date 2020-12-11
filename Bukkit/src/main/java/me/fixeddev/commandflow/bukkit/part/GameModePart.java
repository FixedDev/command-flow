package me.fixeddev.commandflow.bukkit.part;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.stack.ArgumentStack;

import net.kyori.text.TextComponent;
import net.kyori.text.TranslatableComponent;

import org.bukkit.GameMode;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class GameModePart implements ArgumentPart {

    private static final Map<String, GameMode> GAMEMODE_ALIASES = new HashMap<>();

    static {
        for (GameMode gameMode : GameMode.values()) {
            GAMEMODE_ALIASES.put(gameMode.getValue() + "", gameMode);

            String gameModeName = gameMode.name().toLowerCase();

            GAMEMODE_ALIASES.put(gameModeName, gameMode);

            if (gameMode == GameMode.SPECTATOR) {
                GAMEMODE_ALIASES.put("spec", gameMode);
                GAMEMODE_ALIASES.put("sp", gameMode);

                continue;
            }

            GAMEMODE_ALIASES.put(String.valueOf(gameModeName.charAt(0)), gameMode);
        }
    }

    public static void addAlias(String alias, GameMode gameMode) {
        GAMEMODE_ALIASES.put(alias.toLowerCase(), gameMode);
    }

    private final String name;

    public GameModePart(String name) {
        this.name = name;
    }

    @Override
    public List<GameMode> parseValue(CommandContext context, ArgumentStack stack) throws ArgumentParseException {
        String possibleGameModeName = stack.next().toLowerCase();

        GameMode possibleGameMode = GAMEMODE_ALIASES.get(possibleGameModeName);

        if (possibleGameMode == null) {
            throw new ArgumentParseException(TranslatableComponent.of("invalid.gamemode", TextComponent.of(possibleGameModeName)));
        }

        return Collections.singletonList(possibleGameMode);
    }

    @Override
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        if (!stack.hasNext()) {
            return Collections.emptyList();
        }

        String possibleGameModeName = stack.next().toUpperCase();

        return Arrays.stream(GameMode.values())
                .map(gameMode -> gameMode.name().toLowerCase())
                .filter(name -> possibleGameModeName.length() == 0 || name.startsWith(possibleGameModeName))
                .collect(Collectors.toList());
    }

    @Override
    public Type getType() {
        return GameMode.class;
    }

    @Override
    public String getName() {
        return name;
    }

}