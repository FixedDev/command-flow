package me.fixeddev.commandflow.bungee;

import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.NamespaceImpl;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.exception.CommandException;
import me.fixeddev.commandflow.exception.CommandUsage;
import me.fixeddev.commandflow.exception.InvalidSubCommandException;
import me.fixeddev.commandflow.exception.NoMoreArgumentsException;
import me.fixeddev.commandflow.exception.NoPermissionsException;
import me.fixeddev.commandflow.translator.Translator;
import net.kyori.text.Component;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BungeeCommandWrapper extends Command implements TabExecutor {

    private final CommandManager commandManager;
    private final Translator translator;

    private final String[] aliases;
    private final String permission;

    public BungeeCommandWrapper(me.fixeddev.commandflow.command.Command command, CommandManager commandManager,
                                Translator translator) {
        super(command.getName());

        this.commandManager = commandManager;
        this.translator = translator;

        this.aliases = command.getAliases().toArray(new String[0]);
        this.permission = command.getPermission();
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        List<String> argumentLine = new ArrayList<>();
        argumentLine.add(getName());
        argumentLine.addAll(Arrays.asList(args));

        Namespace namespace = new NamespaceImpl();
        namespace.setObject(CommandSender.class, BungeeCommandManager.SENDER_NAMESPACE, commandSender);


        try {
            commandManager.execute(namespace, argumentLine);
        } catch (CommandUsage e) {
            CommandException exceptionToSend = e;
            if (e.getCause() instanceof ArgumentParseException) {
                exceptionToSend = (ArgumentParseException) e.getCause();
            }

           sendMessageToSender(exceptionToSend, commandSender, namespace);

        } catch (InvalidSubCommandException e) {
            sendMessageToSender(e, commandSender, namespace);

            throw new CommandException("An internal parse exception occurred while executing the command " + getName(), e);
        } catch (ArgumentParseException | NoMoreArgumentsException e) {
            sendMessageToSender(e, commandSender, namespace);
        } catch (NoPermissionsException e) {
           sendMessageToSender(e, commandSender, namespace);

        } catch (CommandException e) {
            CommandException exceptionToSend = e;

            if (e.getCause() instanceof CommandException) {
                exceptionToSend = (CommandException) e.getCause();
            }

           sendMessageToSender(exceptionToSend, commandSender, namespace);

            throw new CommandException("An unexpected exception occurred while executing the command " + getName(), exceptionToSend);
        }
    }

    public String getPermission() {
        return permission;
    }

    public String[] getAliases() {
        return aliases;
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        List<String> argumentList = Arrays.asList(strings);

        Namespace namespace = new NamespaceImpl();
        namespace.setObject(CommandSender.class, BungeeCommandManager.SENDER_NAMESPACE, commandSender);

        return commandManager.getSuggestions(namespace, argumentList);
    }

    private void sendMessageToSender(CommandException exception, CommandSender sender, Namespace namespace) {
        Component component = exception.getMessageComponent();
        Component translatedComponent = translator.translate(component, namespace);

        BaseComponent[] components = MessageUtils.kyoriToBungee(translatedComponent);

        sender.sendMessage(components);
    }
}
