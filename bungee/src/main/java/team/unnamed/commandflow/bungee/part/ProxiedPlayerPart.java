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
package team.unnamed.commandflow.bungee.part;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.bungee.BungeeCommandManager;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.part.ArgumentPart;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.stack.ArgumentStack;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
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
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        String last = stack.hasNext() ? stack.next() : null;

        if (last == null) {
            return Collections.emptyList();
        }

        if (ProxyServer.getInstance().getPlayer(last) != null) {
            return Collections.emptyList();
        }

        List<String> names = new ArrayList<>();

        for (ProxiedPlayer player : ProxyServer.getInstance().matchPlayer(last)) {
            names.add(player.getName());
        }

        return names;

    }

    @Override
    public List<? extends ProxiedPlayer> parseValue(CommandContext context, ArgumentStack stack, CommandPart parent) throws ArgumentParseException {
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
            proxiedPlayer = ProxyServer.getInstance().getPlayer(target);

            if (proxiedPlayer == null) {
                throw new ArgumentParseException(Component.translatable("player.offline", Component.text(target)))
                        .setArgument(this);
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
    public String getName() {
        return name;
    }
}
