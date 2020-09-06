package me.fixeddev.commandflow.command;

import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.part.defaults.EmptyPart;
import me.fixeddev.commandflow.part.Parts;
import net.kyori.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleCommand implements Command {

    private final String name;
    private final List<String> aliases;
    private final Component description;
    private final String permission;
    private final Component permissionMessage;
    private final CommandPart mainPart;
    private final Action action;

    public SimpleCommand(String name, List<String> aliases, Component description, String permission, Component permissionMessage, CommandPart mainPart, Action action) {
        this.name = name;
        this.aliases = aliases;
        this.description = description;
        this.permission = permission;
        this.permissionMessage = permissionMessage;
        this.mainPart = mainPart;
        this.action = action;
    }

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
    public @NotNull CommandPart getPart() {
        return mainPart;
    }

    @Override
    public @NotNull Action getAction() {
        return action;
    }

    public static class Builder implements Command.Builder {

        private final String name;
        private List<String> aliases;
        private Component description;
        private String permission;
        private Component permissionMessage;
        private List<CommandPart> parts;
        private Action action;

        public Builder(String name) {
            this.name = name;
            aliases = new ArrayList<>();
            parts = new ArrayList<>();
            action = Action.NULL_ACTION;
        }

        @Override
        public Builder aliases(List<String> aliases) {
            if (aliases == null) {
                throw new IllegalArgumentException("The aliases shouldn't be a null list!");
            }

            this.aliases = aliases;

            return this;
        }

        @Override
        public Builder addAlias(String alias) {
            if (aliases.contains(alias)) {
                return this;
            }

            aliases.add(alias);

            return this;
        }

        @Override
        public Builder description(Component component) {
            if (component == null) {
                throw new IllegalArgumentException("The description shouldn't be a null!");
            }

            this.description = component;

            return this;
        }

        @Override
        public Builder permission(String permission) {
            this.permission = permission;

            return this;
        }

        @Override
        public Builder permissionMessage(Component permissionMessage) {
            if (permissionMessage == null) {
                throw new IllegalArgumentException("The permissionMessage shouldn't be a null!");
            }

            this.permissionMessage = permissionMessage;

            return this;
        }

        @Override
        public Builder part(CommandPart part) {
            if (part == null) {
                throw new IllegalArgumentException("The CommandPart shouldn't be a null!");
            }

            parts.clear();
            parts.add(part);

            return this;
        }

        @Override
        public Builder addParts(CommandPart... parts) {
            List<CommandPart> newParts = Arrays.asList(parts);

            this.parts.addAll(newParts);
            return this;
        }

        @Override
        public Builder addPart(CommandPart part) {
            parts.add(part);

            return this;
        }

        @Override
        public Builder action(Action action) {
            if (action == null) {
                throw new IllegalArgumentException("The Action shouldn't be a null!");
            }

            this.action = action;

            return this;
        }

        @Override
        public Command build() {
            CommandPart part = new EmptyPart("empty-" + name);

            if (!parts.isEmpty()) {
                if (parts.size() == 1) {
                    part = parts.get(0);
                } else {
                    part = Parts.sequential("sequential", parts);
                }
            }

            if (permission == null) {
                permission = "";
            }

            return new SimpleCommand(name, aliases, description, permission, permissionMessage, part, action);
        }
    }
}
