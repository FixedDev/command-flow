package me.fixeddev.commandflow.brigadier;

import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.command.Command;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.CommodoreProvider;
import org.bukkit.plugin.Plugin;

public class BrigadierCommandManager extends BukkitCommandManager {

    private Commodore commodore;
    private CommandBrigadierConverter commandBrigadierConverter;

    public BrigadierCommandManager(CommandManager commandManager, Plugin plugin) {
        super(commandManager, plugin.getName());

        if (isCommodoreSupported()) {
            commodore = CommodoreProvider.getCommodore(plugin);

            commandBrigadierConverter = new CommandBrigadierConverter(commodore);
        }
    }

    public BrigadierCommandManager(Plugin plugin) {
        super(plugin.getName());

        if (isCommodoreSupported()) {
            commodore = CommodoreProvider.getCommodore(plugin);

            commandBrigadierConverter = new CommandBrigadierConverter(commodore);
        }
    }

    private boolean isCommodoreSupported() {
        return CommodoreProvider.isSupported();
    }

    @Override
    public void registerCommand(Command command) {
        manager.registerCommand(command);

        BrigadierCommandWrapper bukkitCommand = new BrigadierCommandWrapper(command,
                this, getTranslator());

        wrapperMap.put(command.getName(), bukkitCommand);
        bukkitCommandMap.register(fallbackPrefix, bukkitCommand);


        if (isCommodoreSupported()) {
            commandBrigadierConverter.registerCommand(command, bukkitCommand);
        }
    }
}
