package me.fixeddev.commandflow.velocity;

import com.velocitypowered.api.command.CommandSource;
import me.fixeddev.commandflow.Authorizer;
import me.fixeddev.commandflow.Namespace;

public class VelocityAuthorizer implements Authorizer {

    @Override
    public boolean isAuthorized(Namespace namespace, String permission) {
        if (permission.isEmpty()) {
            return true;
        }

        CommandSource sender = namespace.getObject(CommandSource.class, VelocityCommandManager.SENDER_NAMESPACE);
        return sender.hasPermission(permission);
    }

}
