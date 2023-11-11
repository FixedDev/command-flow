/*
 * This file is part of commandflow, licensed under the MIT license
 *
 * Copyright (c) 2020-2023 Unnamed Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package team.unnamed.commandflow.bukkit.part;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.part.ArgumentPart;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.stack.ArgumentStack;

import net.kyori.adventure.text.Component;

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
        GAMEMODE_ALIASES.put(alias.toLowerCase(), gameMode);
    }

    @Override
    public List<GameMode> parseValue(CommandContext context, ArgumentStack stack, CommandPart parent) throws ArgumentParseException {
        String possibleGameModeName = stack.next().toLowerCase();

        GameMode possibleGameMode = GAMEMODE_ALIASES.get(possibleGameModeName);

        if (possibleGameMode == null) {
            throw new ArgumentParseException(Component.translatable("invalid.gamemode").args(Component.text(possibleGameModeName)));
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