package team.unnamed.commandflow.bukkit;

import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;
import team.unnamed.commandflow.command.Command;
import team.unnamed.commandflow.translator.Translator;

public class PluginBukkitCommandWrapper extends BukkitCommandWrapper implements PluginIdentifiableCommand {

    private final Plugin plugin;

    public PluginBukkitCommandWrapper(Command command,
                                      BukkitCommandManager dispatcher,
                                      Translator translator,
                                      Plugin plugin) {
        super(command, dispatcher, translator);

        this.plugin = plugin;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }
}
