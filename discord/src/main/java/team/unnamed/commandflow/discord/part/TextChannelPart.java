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
package team.unnamed.commandflow.discord.part;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.discord.DiscordCommandManager;
import team.unnamed.commandflow.discord.utils.ArgumentsUtils;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.part.ArgumentPart;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.stack.ArgumentStack;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.kyori.adventure.text.Component;

import java.util.Collections;
import java.util.List;

public class TextChannelPart implements ArgumentPart {

    private final String name;

    public TextChannelPart(String name) {
        this.name = name;
    }

    @Override
    public List<? extends TextChannel> parseValue(CommandContext context, ArgumentStack stack, CommandPart parent) throws ArgumentParseException {
        Message message = context.getObject(Message.class, DiscordCommandManager.MESSAGE_NAMESPACE);

        String argument = stack.next();

        TextChannel channel = null;

        if (ArgumentsUtils.isChannelMention(argument)) {
            String id = argument.substring(2, argument.length() - 1);
            channel = message.getGuild().getTextChannelById(id);
        }

        if (channel == null && ArgumentsUtils.isValidSnowflake(argument)) {
            channel = message.getGuild().getTextChannelById(argument);
        }

        if (channel == null) {
            String channelName = argument.startsWith("#") ? argument.substring(1) : argument;
            List<TextChannel> channels = message.getGuild().getTextChannelsByName(channelName, true);
            if (channels.size() >= 1) {
                channel = channels.get(0);
            }
        }

        if (channel == null) {
            throw new ArgumentParseException(Component.translatable("unknown.channel"))
                    .setArgument(this);
        }

        return Collections.singletonList(channel);
    }

    @Override
    public String getName() {
        return this.name;
    }
}
