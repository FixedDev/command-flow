package me.fixeddev.commandflow.examples.user;

import me.fixeddev.commandflow.Authorizer;
import me.fixeddev.commandflow.Namespace;

// This class is called to determine if an execution should be authorized
public class UserAuthorizer implements Authorizer {
    @Override
    public boolean isAuthorized(Namespace namespace, String permission) {
        User user = namespace.getObject(User.class, "USER");

        // An object may not be present in the namespace
        // See NamespaceUsage
        if (user == null) {
            return false;
        }

        return user.hasPermission(permission);
    }


}
