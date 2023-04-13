package me.fixeddev.commandflow.discord;

import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.NamespaceImpl;
import me.fixeddev.commandflow.discord.utils.MessageUtils;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.exception.CommandException;
import me.fixeddev.commandflow.exception.CommandUsage;
import me.fixeddev.commandflow.exception.InvalidSubCommandException;
import me.fixeddev.commandflow.exception.NoMoreArgumentsException;
import me.fixeddev.commandflow.exception.NoPermissionsException;
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

        Namespace namespace = new NamespaceImpl();

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
