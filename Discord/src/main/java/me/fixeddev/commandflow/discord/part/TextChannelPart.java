package me.fixeddev.commandflow.discord.part;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.discord.DiscordCommandManager;
import me.fixeddev.commandflow.discord.utils.ArgumentsUtils;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
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
            ArgumentParseException exception = new ArgumentParseException(Component.translatable("unknown.channel"));
            exception.setArgument(this);

            throw exception;
        }

        return Collections.singletonList(channel);
    }

    @Override
    public String getName() {
        return this.name;
    }
}
