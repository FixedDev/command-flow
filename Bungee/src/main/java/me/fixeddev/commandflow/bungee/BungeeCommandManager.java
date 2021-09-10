package me.fixeddev.commandflow.bungee;

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
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class BungeeCommandManager implements CommandManager {

    public static final String SENDER_NAMESPACE = "SENDER";

    protected final CommandManager commandManager;
    protected final Plugin plugin;

    protected final Map<String, BungeeCommandWrapper> wrapperMap;

    public BungeeCommandManager(Plugin plugin) {
        this(new SimpleCommandManager(), plugin);
    }

    public BungeeCommandManager(CommandManager commandManager, Plugin plugin) {
        this.commandManager = commandManager;
        this.plugin = plugin;

        wrapperMap = new HashMap<>();

        setAuthorizer(new BungeeAuthorizer());
        getTranslator().setProvider(new BungeeDefaultTranslationProvider());
        getTranslator().setConverterFunction(LegacyComponentSerializer.legacyAmpersand()::deserialize);
    }

    @Override
    public void registerCommand(Command command) {
        commandManager.registerCommand(command);

        BungeeCommandWrapper bungeeCommandWrapper = new BungeeCommandWrapper(command, commandManager, getTranslator());
        wrapperMap.put(command.getName(), bungeeCommandWrapper);

        plugin.getProxy().getPluginManager().registerCommand(plugin, bungeeCommandWrapper);
    }

    @Override
    public void registerCommand(String label, Command command) {
        commandManager.registerCommand(label, command);
    }


    @Override
    public void registerCommands(List<Command> commandList) {
        for (Command command : commandList) {
            registerCommand(command);
        }
    }

    @Override
    public void unregisterCommand(Command command) {
        commandManager.unregisterCommand(command);

        BungeeCommandWrapper bungeeCommandWrapper = wrapperMap.get(command.getName());
        if (bungeeCommandWrapper != null) {
            plugin.getProxy().getPluginManager().unregisterCommand(bungeeCommandWrapper);
        }
    }

    @Override
    public void unregisterCommands(List<Command> commands) {
        commandManager.unregisterCommands(commands);
    }

    @Override
    public void unregisterAll() {
        commandManager.unregisterAll();
    }

    @Override
    public Set<Command> getCommands() {
        return commandManager.getCommands();
    }

    @Override
    public boolean exists(String commandName) {
        return commandManager.exists(commandName);
    }

    @Override
    public Authorizer getAuthorizer() {
        return commandManager.getAuthorizer();
    }

    @Override
    public void setAuthorizer(Authorizer authorizer) {
        commandManager.setAuthorizer(authorizer);
    }

    @Override
    public InputTokenizer getInputTokenizer() {
        return commandManager.getInputTokenizer();
    }

    @Override
    public void setInputTokenizer(InputTokenizer tokenizer) {
        commandManager.setInputTokenizer(tokenizer);
    }

    @Override
    public Executor getExecutor() {
        return commandManager.getExecutor();
    }

    @Override
    public void setExecutor(Executor executor) {
        commandManager.setExecutor(executor);
    }

    @Override
    public Translator getTranslator() {
        return commandManager.getTranslator();
    }

    @Override
    public void setTranslator(Translator translator) {
        commandManager.setTranslator(translator);
    }

    @Override
    public UsageBuilder getUsageBuilder() {
        return commandManager.getUsageBuilder();
    }

    @Override
    public void setUsageBuilder(UsageBuilder usageBuilder) {
        commandManager.setUsageBuilder(usageBuilder);
    }

    @Override
    public Optional<Command> getCommand(String commandName) {
        return commandManager.getCommand(commandName);
    }

    @Override
    public boolean execute(Namespace accessor, List<String> arguments) throws CommandException {
        return commandManager.execute(accessor, arguments);
    }

    @Override
    public List<String> getSuggestions(Namespace accessor, List<String> arguments) {
        return commandManager.getSuggestions(accessor, arguments);
    }

    @Override
    public boolean execute(Namespace accessor, String line) throws CommandException {
        return commandManager.execute(accessor, line);
    }

    @Override
    public List<String> getSuggestions(Namespace accessor, String line) {
        return commandManager.getSuggestions(accessor, line);
    }

    @Override
    public boolean execute(CommandContext commandContext) throws CommandException {
        return commandManager.execute(commandContext);
    }

    @Override
    public ParseResult parse(Namespace accessor, List<String> arguments) throws CommandException {
        return commandManager.parse(accessor, arguments);
    }

    @Override
    public ParseResult parse(Namespace accessor, String line) throws CommandException {
        return commandManager.parse(accessor, line);
    }
}
