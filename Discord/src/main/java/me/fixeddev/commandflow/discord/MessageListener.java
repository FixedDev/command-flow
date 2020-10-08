package me.fixeddev.commandflow.discord;

import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.NamespaceImpl;
import me.fixeddev.commandflow.command.Command;
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
import net.kyori.text.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

        // removing prefix from the message
        rawMessage = rawMessage.substring(commandPrefix.length());

        String[] rawMessagePart = rawMessage.split(" ");
        String commandName = rawMessagePart[0];

        Optional<Command> optionalCommand = commandManager.getCommand(commandName);

        if (!optionalCommand.isPresent()) {
            return;
        }

        List<String> argumentLine = Arrays.asList(rawMessagePart);

        Namespace namespace = new NamespaceImpl();

        namespace.setObject(Message.class, DiscordCommandManager.MESSAGE_NAMESPACE, message);
        namespace.setObject(Member.class, DiscordCommandManager.MEMBER_NAMESPACE, member);
        namespace.setObject(User.class, DiscordCommandManager.USER_NAMESPACE, user);

        try {
            commandManager.execute(namespace, argumentLine);
        } catch (CommandUsage e) {
            String usage = e.getMessage()
                    .replace("<command>", commandName);
            event.getChannel().sendMessage(usage).queue();
        } catch (InvalidSubCommandException e) {
            sendMessage(namespace, e, channel);

            throw new CommandException("An internal parse exception occurred while executing the command " + commandName, e);
        }
        catch (ArgumentParseException | NoMoreArgumentsException | NoPermissionsException e) {
            sendMessage(namespace, e, channel);
        } catch (CommandException e) {
            CommandException exceptionToSend = e;

            if (e.getCause() instanceof CommandException) {
                exceptionToSend = (CommandException) e.getCause();
            }

            sendMessage(namespace, exceptionToSend, event.getChannel());

            throw new CommandException("An unexpected exception occurred while executing the command " + commandName, exceptionToSend);
        }

    }

    private void sendMessage(Namespace namespace, CommandException exception, TextChannel channel) {

        Component component = exception.getMessageComponent();
        Component translatedComponent = commandManager.getTranslator().translate(component, namespace);

        channel.sendMessage(MessageUtils.componentToString(translatedComponent)).queue();
    }

}
