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
import team.unnamed.commandflow.bukkit.BukkitCommandManager;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.part.ArgumentPart;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.stack.ArgumentStack;
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
