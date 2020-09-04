package me.fixeddev.commandflow.command;

import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.part.EmptyPart;
import me.fixeddev.commandflow.part.SequentialCommandPart;
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
        private CommandPart part;
        private Action action;

        public Builder(String name) {
            this.name = name;
            aliases = new ArrayList<>();
            part = new EmptyPart("empty");
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

            this.part = part;

            return this;
        }

        @Override
        public Builder addParts(CommandPart... parts) {
            List<CommandPart> newParts = Arrays.asList(parts);

            if (this.part instanceof SequentialCommandPart) {
                List<CommandPart> partsList = ((SequentialCommandPart) this.part).getParts();

                partsList.addAll(newParts);
            } else {
                CommandPart oldPart = this.part;
                newParts.add(0, oldPart);

                this.part = new SequentialCommandPart("sequential", newParts);
            }

            return this;
        }

        @Override
        public Builder addPart(CommandPart part) {
            if (this.part instanceof SequentialCommandPart) {
                List<CommandPart> partsList = ((SequentialCommandPart) this.part).getParts();

                partsList.add(part);
            } else {
                CommandPart oldPart = this.part;
                List<CommandPart> parts = new ArrayList<>();

                parts.add(oldPart);
                parts.add(part);

                this.part = new SequentialCommandPart("sequential", parts);
            }

            return this;
        }

        @Override
        public Builder action(Action action) {
            if(action == null){
                throw new IllegalArgumentException("The Action shouldn't be a null!");
            }

            this.action = action;

            return this;
        }

        @Override
        public Command build() {
            return new SimpleCommand(name, aliases, description, permission, permissionMessage, part, action);
        }
    }
}
