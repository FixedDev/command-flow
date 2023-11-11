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
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.kyori.adventure.text.Component;

import java.util.Collections;
import java.util.List;

public class UserPart implements ArgumentPart {

    private final String name;

    public UserPart(String name) {
        this.name = name;
    }

    @Override
    public List<? extends User> parseValue(CommandContext context, ArgumentStack stack, CommandPart parent) throws ArgumentParseException {
        Message message = context.getObject(Message.class, DiscordCommandManager.MESSAGE_NAMESPACE);
        Guild guild = message.getTextChannel().getGuild();

        String target = stack.next();

        User user = null;

        if (ArgumentsUtils.isValidSnowflake(target)) {
            user = guild.getJDA().getUserById(target);
        }

        if (user == null && ArgumentsUtils.isValidTag(target)) {
            user = guild.getJDA().getUserByTag(target);
        }

        if (user == null && ArgumentsUtils.isUserMention(target)) {
            String id = target.substring(3, target.length() - 1);
            user = guild.getJDA().getUserById(id);
        }

        if (user == null) {
            throw new ArgumentParseException(Component.translatable("unknown.user"))
                    .setArgument(this);
        }

        return Collections.singletonList(user);
    }

    @Override
    public String getName() {
        return name;
    }
}
