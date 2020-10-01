package me.fixeddev.commandflow;

public interface Authorizer {
    /**
     * If an executor has certain permission, meaning that it is authorized.
     *
     * @param namespace  The {@link Namespace} that contains injected values like the source.
     * @param permission The permission to check against.
     * @return If the source is authorized to this permission, true if the permission is empty.
     */
    boolean isAuthorized(Namespace namespace, String permission);
}
