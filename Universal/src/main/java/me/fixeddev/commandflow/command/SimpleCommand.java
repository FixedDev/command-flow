package me.fixeddev.commandflow.command;

import me.fixeddev.commandflow.part.SequentialCommandPart;
import net.kyori.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SimpleCommand implements Command{

    private String name;
    private List<String> aliases;
    private Component description;
    private String permission;
    private Component permissionMessage;
    private SequentialCommandPart mainPart;
    private Action action;

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull List<String> getAliases() {
        return aliases;
    }

    @Override
    public Component getDescription() {
        return description;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public Component getPermissionMessage() {
        return permissionMessage;
    }

    @Override
    public @NotNull SequentialCommandPart getPart() {
        return mainPart;
    }

    @Override
    public @NotNull Action getAction() {
        return action;
    }
}
