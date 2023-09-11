package me.fixeddev.commandflow.minestom;

import me.fixeddev.commandflow.Authorizer;
import me.fixeddev.commandflow.Namespace;
import net.minestom.server.command.CommandSender;

public class MinestomAuthorizer implements Authorizer {
    @Override
    public boolean isAuthorized(Namespace namespace, String permission) {
        if (permission.isEmpty()) return true;

        CommandSender sender = namespace.getObject(CommandSender.class, MinestomCommandManager.SENDER_NAMESPACE);

        return sender.hasPermission(permission);
    }
}