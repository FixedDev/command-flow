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
package team.unnamed.commandflow.discord;

import team.unnamed.commandflow.CommandManager;
import team.unnamed.commandflow.Namespace;
import team.unnamed.commandflow.discord.utils.MessageUtils;
import team.unnamed.commandflow.exception.CommandException;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kyori.adventure.text.Component;

public class MessageListener extends ListenerAdapter {

    private final CommandManager commandManager;
    private final String commandPrefix;

    public MessageListener(CommandManager commandManager, String commandPrefix) {
        this.commandManager = commandManager;
        this.commandPrefix = commandPrefix;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        Member member = event.getMember();
        User user = event.getAuthor();
        Message message = event.getMessage();
        TextChannel channel = event.getChannel();

        String rawMessage = event.getMessage().getContentRaw();

        if (!rawMessage.startsWith(commandPrefix)) {
            return;
        }

        rawMessage = rawMessage.substring(commandPrefix.length());

        String label = rawMessage.substring(0, rawMessage.indexOf(" "));

        Namespace namespace = Namespace.create();

        namespace.setObject(Message.class, DiscordCommandManager.MESSAGE_NAMESPACE, message);
        namespace.setObject(Member.class, DiscordCommandManager.MEMBER_NAMESPACE, member);
        namespace.setObject(User.class, DiscordCommandManager.USER_NAMESPACE, user);
        namespace.setObject(TextChannel.class, DiscordCommandManager.CHANNEL_NAMESPACE, channel);
        namespace.setObject(String.class, "label", label);

        try {
            commandManager.execute(namespace, rawMessage.substring(commandPrefix.length()));
        } catch (CommandException e) {
            CommandException exceptionToSend = e;

            if (e.getCause() instanceof CommandException) {
                exceptionToSend = (CommandException) e.getCause();
            }

            sendMessage(namespace, exceptionToSend);

            throw new CommandException("An unexpected exception occurred while executing the command " + e.getCommand().getName(), exceptionToSend);
        }

    }

    protected static void sendMessage(Namespace namespace, CommandException exception) {
        CommandManager commandManager = namespace.getObject(CommandManager.class, "commandManager");
        TextChannel channel = namespace.getObject(TextChannel.class, DiscordCommandManager.CHANNEL_NAMESPACE);

        Component component = exception.getMessageComponent();
        Component translatedComponent = commandManager.getTranslator().translate(component, namespace);

        channel.sendMessage(MessageUtils.componentToString(translatedComponent)).queue();
    }

}
