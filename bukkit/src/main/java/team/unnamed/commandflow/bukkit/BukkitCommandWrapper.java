package team.unnamed.commandflow.bukkit;


import team.unnamed.commandflow.Authorizer;
import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.CommandManager;
import team.unnamed.commandflow.Namespace;
import team.unnamed.commandflow.SimpleCommandContext;
import team.unnamed.commandflow.exception.CommandException;
import team.unnamed.commandflow.translator.Translator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BukkitCommandWrapper extends Command {
    protected final CommandManager commandManager;
    protected final Component permissionMessage;
    protected final Translator translator;

    public BukkitCommandWrapper(team.unnamed.commandflow.command.Command command,
                                CommandManager dispatcher,
                                Translator translator) {
        super(command.getName());

        this.setAliases(command.getAliases());

        CommandContext fakeContext = new SimpleCommandContext(Namespace.create(), new ArrayList<>());
        fakeContext.setCommand(command, "<command>");

        this.setUsage(LegacyComponentSerializer.legacyAmpersand().serialize(dispatcher.getUsageBuilder().getUsage(fakeContext)));

        if (command.getDescription() != null) {
            Component translatedDescription = translator.translate(command.getDescription(), Namespace.create());

            this.setDescription(LegacyComponentSerializer.legacyAmpersand().serialize(translatedDescription));
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

        Namespace namespace = Namespace.create();

        namespace.setObject(CommandSender.class, BukkitCommandManager.SENDER_NAMESPACE, commandSender);
        namespace.setObject(String.class, "label", label);


        try {
            return commandManager.execute(namespace, argumentLine);
        } catch (CommandException e) {
            Throwable exceptionToSend = e;

            Throwable cause = e.getCause();

            if (cause instanceof org.bukkit.command.CommandException) {
                throw (org.bukkit.command.CommandException) cause;
            }

            if (cause != null) {
                exceptionToSend = cause;
            }

            throw new org.bukkit.command.CommandException("An unexpected exception occurred while executing the command " + label, exceptionToSend);
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        List<String> argumentLine = new ArrayList<>(Arrays.asList(args));
        argumentLine.add(0, alias);

        Namespace namespace = Namespace.create();
        namespace.setObject(CommandSender.class, BukkitCommandManager.SENDER_NAMESPACE, sender);

        return commandManager.getSuggestions(namespace, argumentLine);
    }

    @Override
    public boolean testPermission(@NotNull CommandSender target) {
        if (!testPermissionSilent(target)) {
            Namespace namespace = Namespace.create();
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

        Namespace namespace = Namespace.create();
        namespace.setObject(CommandSender.class, BukkitCommandManager.SENDER_NAMESPACE, target);

        return authorizer.isAuthorized(namespace, getPermission());
    }

    protected static void sendMessageToSender(CommandException exception, Namespace namespace) {
        CommandManager commandManager = namespace.getObject(CommandManager.class, "commandManager");
        CommandSender sender = namespace.getObject(CommandSender.class, BukkitCommandManager.SENDER_NAMESPACE);

        Component component = exception.getMessageComponent();
        Component translatedComponent = commandManager.getTranslator().translate(component, namespace);

        BaseComponent[] components = MessageUtils.kyoriToBungee(translatedComponent);

        MessageUtils.sendMessage(sender, components);
    }
}
