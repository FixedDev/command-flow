package me.fixeddev.commandflow.discord;

import me.fixeddev.commandflow.Authorizer;
import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.SimpleCommandManager;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.exception.CommandException;
import me.fixeddev.commandflow.executor.Executor;
import me.fixeddev.commandflow.input.InputTokenizer;
import me.fixeddev.commandflow.translator.Translator;
import me.fixeddev.commandflow.usage.UsageBuilder;
import net.dv8tion.jda.api.JDA;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class DiscordCommandManager implements CommandManager {

    public static final String MESSAGE_NAMESPACE = "MESSAGE";
    public static final String MEMBER_NAMESPACE = "MEMBER";

    private final CommandManager commandManager;

    public DiscordCommandManager(JDA client, String commandPrefix) {
        this(new SimpleCommandManager(), client, commandPrefix);
    }

    public DiscordCommandManager(CommandManager commandManager, JDA client, String commandPrefix) {
        this.commandManager = commandManager;

        client.addEventListener(new MessageListener(commandManager, commandPrefix));

        setAuthorizer(new DiscordAuthorizer());
        getTranslator().setProvider(new DiscordDefaultTranslationProvider());
    }

    @Override
    public void registerCommand(Command command) {
        commandManager.registerCommand(command);
    }

    @Override
    public void registerCommands(List<Command> commandList) {
        commandManager.registerCommands(commandList);
    }

    @Override
    public void unregisterCommand(Command command) {
        commandManager.unregisterCommand(command);
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
}
