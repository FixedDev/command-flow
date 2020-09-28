package me.fixeddev.commandflow.bukkit;

import me.fixeddev.commandflow.Authorizer;
import me.fixeddev.commandflow.Namespace;
import org.bukkit.command.CommandSender;

public class BukkitAuthorizer implements Authorizer {
    @Override
    public boolean isAuthorized(Namespace namespace, String permission) {
        if (permission.isEmpty()) {
            return true;
        }

        CommandSender sender = namespace.getObject(CommandSender.class, BukkitCommandManager.SENDER_NAMESPACE);

        return sender.hasPermission(permission);
    }
}
