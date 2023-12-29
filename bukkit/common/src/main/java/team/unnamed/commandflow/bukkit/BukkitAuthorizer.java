package team.unnamed.commandflow.bukkit;

import team.unnamed.commandflow.Authorizer;
import team.unnamed.commandflow.Namespace;
import org.bukkit.command.CommandSender;

public class BukkitAuthorizer implements Authorizer {

    @Override
    public boolean isAuthorized(Namespace namespace, String permission) {
        if (permission.isEmpty()) {
            return true;
        }

        CommandSender sender = namespace.getObject(CommandSender.class, BukkitCommonConstants.SENDER_NAMESPACE);

        return sender.hasPermission(permission);
    }

}
