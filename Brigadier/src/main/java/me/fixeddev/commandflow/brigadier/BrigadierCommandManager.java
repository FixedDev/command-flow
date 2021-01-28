package me.fixeddev.commandflow.brigadier;

import com.mojang.brigadier.tree.LiteralCommandNode;
import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.command.Command;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.CommodoreProvider;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BrigadierCommandManager extends BukkitCommandManager {

    private Commodore commodore;
    private CommandBrigadierConverter commandBrigadierConverter;
    private final Plugin plugin;

    private final Map<Command, List<LiteralCommandNode<Object>>> brigadierNodes;

    public BrigadierCommandManager(CommandManager commandManager, Plugin plugin) {
        super(commandManager, plugin.getName());

        this.plugin = plugin;
        this.brigadierNodes = new HashMap<>();

        if (isCommodoreSupported()) {
            commodore = CommodoreProvider.getCommodore(plugin);

            commandBrigadierConverter = new CommandBrigadierConverter(commodore);
        }
    }

    public BrigadierCommandManager(Plugin plugin) {
        super(plugin.getName());

        this.plugin = plugin;
        this.brigadierNodes = new HashMap<>();

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
            brigadierNodes.put(command, commandBrigadierConverter.registerCommand(command, plugin, bukkitCommand));
        }
    }

    @Override
    public void unregisterCommand(Command command) {
        super.unregisterCommand(command);

        if (isCommodoreSupported()) {
            List<LiteralCommandNode<Object>> nodes = brigadierNodes.get(command);

            if (nodes == null) {
                return;
            }

            commandBrigadierConverter.unregisterCommand(nodes);
        }
    }
}
