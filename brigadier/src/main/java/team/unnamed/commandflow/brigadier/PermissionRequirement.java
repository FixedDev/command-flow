package team.unnamed.commandflow.brigadier;

import team.unnamed.commandflow.Authorizer;
import team.unnamed.commandflow.Namespace;
import team.unnamed.commandflow.bukkit.BukkitCommandManager;
import me.lucko.commodore.Commodore;
import org.bukkit.command.CommandSender;

import java.util.function.Function;
import java.util.function.Predicate;

public class PermissionRequirement<T, V> implements Predicate<T> {

    private final String permission;
    private final Authorizer authorizer;
    private final Function<T, V> senderMapping;

    public PermissionRequirement(String permission, Authorizer authorizer, Function<T, V> senderMapping) {
        this.permission = permission;
        this.authorizer = authorizer;
        this.senderMapping = senderMapping;
    }

    @Override
    public boolean test(T o) {
        V sender = senderMapping.apply(o);

        Namespace namespace = Namespace.create();
        //noinspection unchecked
        namespace.setObject((Class<V>) sender.getClass(), "sender", sender);

        return authorizer.isAuthorized(namespace, permission);
    }

}
