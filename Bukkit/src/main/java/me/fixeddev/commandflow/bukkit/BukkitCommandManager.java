package me.fixeddev.commandflow.bukkit;

import me.fixeddev.commandflow.Authorizer;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.ParseResult;
import me.fixeddev.commandflow.SimpleCommandManager;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.exception.CommandException;
import me.fixeddev.commandflow.executor.Executor;
import me.fixeddev.commandflow.input.InputTokenizer;
import me.fixeddev.commandflow.translator.Translator;
import me.fixeddev.commandflow.usage.UsageBuilder;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;

public class BukkitCommandManager implements CommandManager {
    public static final String SENDER_NAMESPACE = "SENDER";

    protected CommandManager manager;
    protected CommandMap bukkitCommandMap;
    protected final String fallbackPrefix;

    protected final Map<String, BukkitCommandWrapper> wrapperMap;

    public BukkitCommandManager(CommandManager delegate, String fallbackPrefix) {
        this.manager = delegate;
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

    public BukkitCommandManager(String fallbackPrefix) {
        this(new SimpleCommandManager(), fallbackPrefix);

        setAuthorizer(new BukkitAuthorizer());
        getTranslator().setProvider(new BukkitDefaultTranslationProvider());
        getTranslator().setConverterFunction(LegacyComponentSerializer.INSTANCE::deserialize);
    }

    public void registerCommand(Command command) {
        manager.registerCommand(command);

        BukkitCommandWrapper bukkitCommand = new BukkitCommandWrapper(command,
                this, getTranslator());

        for (String alias : command.getAliases()) {
            registerCommand(fallbackPrefix + ":" + alias, command);
        }

        wrapperMap.put(command.getName(), bukkitCommand);
        bukkitCommandMap.register(fallbackPrefix, bukkitCommand);
    }

    @Override
    public void registerCommand(String label, Command command) {
        manager.registerCommand(label, command);
    }

    public void registerCommands(List<Command> commandList) {
        for (Command command : commandList) {
            registerCommand(command);
        }
    }

    @Override
    public void unregisterCommand(Command command) {
        manager.unregisterCommand(command);

        BukkitCommandWrapper wrapper = wrapperMap.get(command.getName());
        if (wrapper != null) {
            wrapper.unregister(bukkitCommandMap);
        }
    }

    @Override
    public void unregisterCommands(List<Command> commands) {
        for (Command command : commands) {
            unregisterCommand(command);
        }
    }

    @Override
    public void unregisterAll() {
        Set<Command> commands = getCommands();

        for (Command command : commands) {
            unregisterCommand(command);
        }
    }

    @Override
    public Set<Command> getCommands() {
        return manager.getCommands();
    }

    @Override
    public boolean exists(String commandName) {
        return manager.exists(commandName);
    }

    @Override
    public Authorizer getAuthorizer() {
        return manager.getAuthorizer();
    }

    @Override
    public void setAuthorizer(Authorizer authorizer) {
        manager.setAuthorizer(authorizer);
    }

    @Override
    public InputTokenizer getInputTokenizer() {
        return manager.getInputTokenizer();
    }

    @Override
    public void setInputTokenizer(InputTokenizer tokenizer) {
        manager.setInputTokenizer(tokenizer);
    }

    @Override
    public Executor getExecutor() {
        return manager.getExecutor();
    }

    @Override
    public void setExecutor(Executor executor) {
        manager.setExecutor(executor);
    }

    @Override
    public Translator getTranslator() {
        return manager.getTranslator();
    }

    @Override
    public void setTranslator(Translator translator) {
        manager.setTranslator(translator);
    }

    @Override
    public UsageBuilder getUsageBuilder() {
        return manager.getUsageBuilder();
    }

    @Override
    public void setUsageBuilder(UsageBuilder usageBuilder) {
        manager.setUsageBuilder(usageBuilder);
    }

    @Override
    public Optional<Command> getCommand(String commandName) {
        return manager.getCommand(commandName);
    }

    @Override
    public boolean execute(Namespace accessor, List<String> arguments) throws CommandException {
        return manager.execute(accessor, arguments);
    }

    @Override
    public List<String> getSuggestions(Namespace accessor, List<String> arguments) {
        return manager.getSuggestions(accessor, arguments);
    }

    @Override
    public boolean execute(Namespace accessor, String line) throws CommandException {
        return manager.execute(accessor, line);
    }

    @Override
    public List<String> getSuggestions(Namespace accessor, String line) {
        return manager.getSuggestions(accessor, line);
    }

    @Override
    public boolean execute(CommandContext commandContext) throws CommandException {
        return manager.execute(commandContext);
    }

    @Override
    public ParseResult parse(Namespace accessor, List<String> arguments) throws CommandException {
        return manager.parse(accessor, arguments);
    }

    @Override
    public ParseResult parse(Namespace accessor, String line) throws CommandException {
        return manager.parse(accessor, line);
    }
}
