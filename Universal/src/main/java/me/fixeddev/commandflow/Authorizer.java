package me.fixeddev.commandflow;

public interface Authorizer {
    boolean isAuthorized(Namespace namespace, String permission);
}
