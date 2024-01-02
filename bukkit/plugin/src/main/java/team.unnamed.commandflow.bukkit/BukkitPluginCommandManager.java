package team.unnamed.commandflow.bukkit;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import team.unnamed.commandflow.CommandManager;
import team.unnamed.commandflow.SimpleCommandManager;
import team.unnamed.commandflow.command.Command;

public class BukkitPluginCommandManager extends BukkitCommandManager {

    protected final String fallbackPrefix;

    protected BukkitCommandExecutor commandExecutor;

    public BukkitPluginCommandManager(CommandManager delegate, @NotNull BukkitCommandExecutor commandExecutor, String fallbackPrefix) {
        super(delegate);
        this.fallbackPrefix = fallbackPrefix;
        this.commandExecutor = commandExecutor;
    }

    public BukkitPluginCommandManager(String fallbackPrefix) {
        //noinspection DataFlowIssue
        this(new SimpleCommandManager(), null, fallbackPrefix);

        commandExecutor = new DefaultBukkitCommandExecutor(this);
        setAuthorizer(new BukkitAuthorizer());
        getTranslator().setProvider(new BukkitDefaultTranslationProvider());
        getTranslator().setConverterFunction(LegacyComponentSerializer.legacyAmpersand()::deserialize);
    }

    @Override
    protected void _register(Command command) {
        PluginCommand pluginCommand = Bukkit.getPluginCommand(command.getName());

        if (pluginCommand != null) {
            pluginCommand.setExecutor(commandExecutor);
            pluginCommand.setTabCompleter(commandExecutor);
        }

        for (String alias : command.getAliases()) {
            registerCommand(fallbackPrefix + ":" + alias, command);
        }

        registerCommand(fallbackPrefix + ":" + command.getName(), command);
    }

    @Override
    protected void _unregister(Command command) {
        PluginCommand pluginCommand = Bukkit.getPluginCommand(command.getName());

        if (pluginCommand != null) {
            pluginCommand.setExecutor(null);
            pluginCommand.setTabCompleter(null);
        }
    }

}
