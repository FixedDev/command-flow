package me.fixeddev.commandflow.brigadier;

import me.fixeddev.commandflow.Authorizer;
import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.lucko.commodore.Commodore;
import org.bukkit.command.CommandSender;

import java.util.function.Predicate;

public class PermissionRequirement implements Predicate<Object> {

    private final String permission;
    private final Authorizer authorizer;
    private final Commodore commodore;

    public PermissionRequirement(String permission, Authorizer authorizer, Commodore commodore) {
        this.permission = permission;
        this.authorizer = authorizer;
        this.commodore = commodore;
    }

    @Override
    public boolean test(Object o) {
        CommandSender sender = commodore.getBukkitSender(o);

        Namespace namespace = Namespace.create();
        namespace.setObject(CommandSender.class, BukkitCommandManager.SENDER_NAMESPACE, sender);

        return authorizer.isAuthorized(namespace, permission);
    }

}
