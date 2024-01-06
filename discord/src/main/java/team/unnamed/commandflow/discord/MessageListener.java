package team.unnamed.commandflow.discord;

import team.unnamed.commandflow.CommandManager;
import team.unnamed.commandflow.Namespace;
import team.unnamed.commandflow.discord.utils.MessageUtils;
import team.unnamed.commandflow.exception.CommandException;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
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
    public void onMessageReceived(MessageReceivedEvent event) {

        if(!(event.getChannel() instanceof TextChannel)){
            return;
        }

        Member member = event.getMember();
        User user = event.getAuthor();
        Message message = event.getMessage();
        TextChannel channel = event.getChannel().asTextChannel();

        String rawMessage = event.getMessage().getContentRaw();

        if (!rawMessage.startsWith(commandPrefix)) {
            return;
        }

        rawMessage = rawMessage.substring(commandPrefix.length());

        String label = rawMessage;

        if(label.indexOf(" ") > 0){
            label = rawMessage.substring(0, rawMessage.indexOf(" "));
        }

        Namespace namespace = Namespace.create();

        namespace.setObject(Message.class, DiscordCommandManager.MESSAGE_NAMESPACE, message);
        namespace.setObject(Member.class, DiscordCommandManager.MEMBER_NAMESPACE, member);
        namespace.setObject(User.class, DiscordCommandManager.USER_NAMESPACE, user);
        namespace.setObject(TextChannel.class, DiscordCommandManager.CHANNEL_NAMESPACE, channel);
        namespace.setObject(String.class, "label", label);

        try {
            commandManager.execute(namespace, rawMessage);
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
