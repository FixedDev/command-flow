package me.fixeddev.commandflow.bukkit;


import me.fixeddev.commandflow.Authorizer;
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
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BukkitCommandWrapper extends Command {
    private final CommandManager commandManager;
    private final Component permissionMessage;
    private final Translator translator;

    public BukkitCommandWrapper(me.fixeddev.commandflow.command.Command command,
                                CommandManager dispatcher,
                                Translator translator) {
        super(command.getName());

        this.setAliases(command.getAliases());

        if (command.getDescription() != null) {
            Component translatedDescription = translator.translate(command.getDescription(), new NamespaceImpl());

            this.setDescription(LegacyComponentSerializer.INSTANCE.serialize(translatedDescription));
        }

        //this.setUsage(UsageBuilder.getUsageForCommand(null, command, "<command>"));

        this.setPermission(command.getPermission());

        this.permissionMessage = command.getPermissionMessage();
        this.commandManager = dispatcher;
        this.translator = translator;
    }


    @Override
    public boolean execute(CommandSender commandSender, String label, String[] args) {
        List<String> argumentLine = new ArrayList<>();

        argumentLine.add(label);
        argumentLine.addAll(Arrays.asList(args));

        Namespace namespace = new NamespaceImpl();
        namespace.setObject(CommandSender.class, BukkitCommandManager.SENDER_NAMESPACE, commandSender);

        try {
            if (commandManager.execute(namespace, argumentLine)) {
                return true;
            }
        } catch (CommandUsage e) {
            CommandException exceptionToSend = e;
            if (e.getCause() instanceof ArgumentParseException) {
                exceptionToSend = (ArgumentParseException) e.getCause();
            }

            sendMessageToSender(exceptionToSend, commandSender, namespace);

            return true;
        } catch (ArgumentParseException | NoMoreArgumentsException e) {
            sendMessageToSender(e, commandSender, namespace);

            if (!(e instanceof InvalidSubCommandException)) {
                throw new org.bukkit.command.CommandException("An internal parse exception occurred while executing the command " + label, e);
            }
        } catch (NoPermissionsException e) {
            sendMessageToSender(e, commandSender, namespace);

            return true;

        } catch (CommandException e) {
            sendMessageToSender(e, commandSender, namespace);
            throw new org.bukkit.command.CommandException("An unexpected exception occurred while executing the command " + label, e);
        }

        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        List<String> argumentLine = new ArrayList<>(Arrays.asList(args));
        argumentLine.add(0, alias);

        Namespace namespace = new NamespaceImpl();
        namespace.setObject(CommandSender.class, BukkitCommandManager.SENDER_NAMESPACE, sender);

        return commandManager.getSuggestions(namespace, argumentLine);
    }

    @Override
    public boolean testPermission(@NotNull CommandSender target) {
        if (!testPermissionSilent(target)) {
            Namespace namespace = new NamespaceImpl();
            namespace.setObject(CommandSender.class, BukkitCommandManager.SENDER_NAMESPACE, target);

            Component translatedPermissionMessage = translator.translate(permissionMessage, namespace);

            BaseComponent[] components = MessageUtils.kyoriToBungee(translatedPermissionMessage);
            MessageUtils.sendMessage(target, components);

            return false;
        }

        return true;
    }

    @Override
    // Ignored value, the command manager authorizer manages this
    public boolean testPermissionSilent(CommandSender target) {
        Authorizer authorizer = commandManager.getAuthorizer();

        Namespace namespace = new NamespaceImpl();
        namespace.setObject(CommandSender.class, BukkitCommandManager.SENDER_NAMESPACE, target);

        return authorizer.isAuthorized(namespace, getPermission());
    }

    private void sendMessageToSender(CommandException exception, CommandSender sender, Namespace namespace) {
        Component component = exception.getMessageComponent();
        Component translatedComponent = translator.translate(component, namespace);

        BaseComponent[] components = MessageUtils.kyoriToBungee(translatedComponent);

        MessageUtils.sendMessage(sender, components);
    }
}
