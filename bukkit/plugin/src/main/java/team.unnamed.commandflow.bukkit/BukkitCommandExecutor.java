package team.unnamed.commandflow.bukkit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import team.unnamed.commandflow.CommandManager;
import team.unnamed.commandflow.Namespace;
import team.unnamed.commandflow.exception.CommandException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface BukkitCommandExecutor extends CommandExecutor, TabCompleter {
    @Override
    default boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        List<String> argumentLine = new ArrayList<>();

        argumentLine.add(label);
        argumentLine.addAll(Arrays.asList(args));

        Namespace namespace = Namespace.create();

        namespace.setObject(CommandSender.class, BukkitCommonConstants.SENDER_NAMESPACE, sender);
        namespace.setObject(String.class, "label", label);

        try {
            return commandManager().execute(namespace, argumentLine);
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
    default List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> argumentLine = new ArrayList<>(Arrays.asList(args));
        argumentLine.add(0, alias);

        Namespace namespace = Namespace.create();
        namespace.setObject(CommandSender.class, BukkitCommonConstants.SENDER_NAMESPACE, sender);

        return commandManager().getSuggestions(namespace, argumentLine);
    }

    CommandManager commandManager();

}
