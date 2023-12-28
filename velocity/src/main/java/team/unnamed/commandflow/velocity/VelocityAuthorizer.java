package team.unnamed.commandflow.velocity;

import com.velocitypowered.api.command.CommandSource;
import team.unnamed.commandflow.Authorizer;
import team.unnamed.commandflow.Namespace;

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
