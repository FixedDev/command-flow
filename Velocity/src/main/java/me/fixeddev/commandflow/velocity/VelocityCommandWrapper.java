package me.fixeddev.commandflow.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.command.SimpleCommand;
import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.NamespaceImpl;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.exception.CommandException;
import me.fixeddev.commandflow.exception.CommandUsage;
import me.fixeddev.commandflow.exception.InvalidSubCommandException;
import me.fixeddev.commandflow.exception.NoMoreArgumentsException;
import me.fixeddev.commandflow.exception.NoPermissionsException;
import me.fixeddev.commandflow.translator.Translator;
import net.kyori.text.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VelocityCommandWrapper implements RawCommand {

    protected final CommandManager commandManager;
    protected final Translator translator;

    protected final String[] aliases;
    protected final String permission;
    protected final String command;

    public VelocityCommandWrapper(Command command, CommandManager commandManager,
                                  Translator translator) {

        this.command = command.getName();
        this.commandManager = commandManager;
        this.translator = translator;

        this.aliases = command.getAliases().toArray(new String[0]);
        this.permission = command.getPermission();
    }


    @Override
    public void execute(Invocation invocation) {
        CommandSource commandSource = invocation.source();
        String argumentLine = invocation.alias() + invocation.arguments();

        Namespace namespace = new NamespaceImpl();
        namespace.setObject(CommandSource.class, VelocityCommandManager.SENDER_NAMESPACE, commandSource);

        try {
            commandManager.execute(namespace, argumentLine);
        } catch (CommandUsage e) {
            CommandException exceptionToSend = e;
            if (e.getCause() instanceof ArgumentParseException) {
                exceptionToSend = (ArgumentParseException) e.getCause();
            }

            sendMessageToSender(exceptionToSend, commandSource, namespace);

        } catch (InvalidSubCommandException e) {
            sendMessageToSender(e, commandSource, namespace);

            throw new CommandException("An internal parse exception occurred while executing the command " + this.command, e);
        } catch (ArgumentParseException | NoMoreArgumentsException | NoPermissionsException e) {
            sendMessageToSender(e, commandSource, namespace);
        } catch (CommandException e) {
            CommandException exceptionToSend = e;

            if (e.getCause() instanceof CommandException) {
                exceptionToSend = (CommandException) e.getCause();
            }

            sendMessageToSender(exceptionToSend, commandSource, namespace);

            throw new CommandException("An unexpected exception occurred while executing the command " + this.command, exceptionToSend);
        }
    }

    public String getPermission() {
        return permission;
    }

    public String[] getAliases() {
        return aliases;
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        CommandSource commandSource = invocation.source();
        String argumentLine = invocation.arguments();

        Namespace namespace = new NamespaceImpl();
        namespace.setObject(CommandSource.class, VelocityCommandManager.SENDER_NAMESPACE, commandSource);

        return commandManager.getSuggestions(namespace, argumentLine);
    }

    private void sendMessageToSender(CommandException exception, CommandSource commandSource, Namespace namespace) {
        Component component = exception.getMessageComponent();
        Component translatedComponent = translator.translate(component, namespace);

        commandSource.sendMessage(MessageUtils.kyoriToVelocityKyori(translatedComponent));
    }
}
