package bukkit;

import team.unnamed.commandflow.CommandManager;
import team.unnamed.commandflow.SimpleCommandManager;
import team.unnamed.commandflow.bukkit.BukkitAuthorizer;
import team.unnamed.commandflow.bukkit.BukkitCommandManager;
import team.unnamed.commandflow.bukkit.BukkitDefaultTranslationProvider;
import team.unnamed.commandflow.command.Command;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class BukkitMapCommandManager extends BukkitCommandManager {

    public static final String SENDER_NAMESPACE = "SENDER";

    protected CommandMap bukkitCommandMap;
    protected final String fallbackPrefix;

    protected final Map<String, BukkitCommandWrapper> wrapperMap;

    public BukkitMapCommandManager(CommandManager delegate, String fallbackPrefix) {
        super(delegate);
        this.fallbackPrefix = fallbackPrefix;
        wrapperMap = new HashMap<>();

        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);

            bukkitCommandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to get command map: ", ex);
        }

    }

    public BukkitMapCommandManager(String fallbackPrefix) {
        this(new SimpleCommandManager(), fallbackPrefix);

        setAuthorizer(new BukkitAuthorizer());
        getTranslator().setProvider(new BukkitDefaultTranslationProvider());
        getTranslator().setConverterFunction(LegacyComponentSerializer.legacyAmpersand()::deserialize);
    }

    @Override
    protected void _register(Command command) {
        BukkitCommandWrapper bukkitCommand = new BukkitCommandWrapper(command,
                this, getTranslator());

        for (String alias : command.getAliases()) {
            registerCommand(fallbackPrefix + ":" + alias, command);
        }

        registerCommand(fallbackPrefix + ":" + command.getName(), command);

        wrapperMap.put(command.getName(), bukkitCommand);
        bukkitCommandMap.register(fallbackPrefix, bukkitCommand);
    }

    @Override
    protected void _unregister(Command command) {
        BukkitCommandWrapper wrapper = wrapperMap.get(command.getName());
        if (wrapper != null) {
            wrapper.unregister(bukkitCommandMap);
        }
    }

}
