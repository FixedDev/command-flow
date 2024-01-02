package team.unnamed.commandflow.bukkit;

import team.unnamed.commandflow.CommandManager;

public class DefaultBukkitCommandExecutor implements BukkitCommandExecutor {

    private final CommandManager commandManager;

    public DefaultBukkitCommandExecutor(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public CommandManager commandManager() {
        return commandManager;
    }
}
