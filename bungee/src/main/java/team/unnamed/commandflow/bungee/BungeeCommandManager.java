package team.unnamed.commandflow.bungee;

import team.unnamed.commandflow.Authorizer;
import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.CommandManager;
import team.unnamed.commandflow.ErrorHandler;
import team.unnamed.commandflow.Namespace;
import team.unnamed.commandflow.ParseResult;
import team.unnamed.commandflow.SimpleCommandManager;
import team.unnamed.commandflow.command.Command;
import team.unnamed.commandflow.command.modifiers.FallbackCommandModifiers;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.exception.CommandException;
import team.unnamed.commandflow.exception.CommandUsage;
import team.unnamed.commandflow.exception.InvalidSubCommandException;
import team.unnamed.commandflow.exception.NoMoreArgumentsException;
import team.unnamed.commandflow.exception.NoPermissionsException;
import team.unnamed.commandflow.executor.Executor;
import team.unnamed.commandflow.input.InputTokenizer;
import team.unnamed.commandflow.translator.Translator;
import team.unnamed.commandflow.usage.UsageBuilder;
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

        getErrorHandler().addExceptionHandler(CommandUsage.class, (namespace, e) -> {
            CommandException exceptionToSend = e;
            if (e.getCause() instanceof ArgumentParseException) {
                exceptionToSend = (ArgumentParseException) e.getCause();
            }

            BungeeCommandWrapper.sendMessageToSender(exceptionToSend, namespace);

            return false;
        });


        ErrorHandler.ErrorConsumer<CommandException> commonHandler = (namespace, e) -> {
            BungeeCommandWrapper.sendMessageToSender(e, namespace);

            return true;
        };

        getErrorHandler().addExceptionHandler(InvalidSubCommandException.class, commonHandler);
        getErrorHandler().addExceptionHandler(ArgumentParseException.class, commonHandler);
        getErrorHandler().addExceptionHandler(NoMoreArgumentsException.class, commonHandler);
        getErrorHandler().addExceptionHandler(NoPermissionsException.class, commonHandler);

        getErrorHandler().addExceptionHandler(CommandException.class, (namespace, e) -> {
            CommandException exceptionToSend = e;

            if (e.getCause() instanceof CommandException) {
                exceptionToSend = (CommandException) e.getCause();
            }
            String label = namespace.getObject(String.class, "label");

            BungeeCommandWrapper.sendMessageToSender(e, namespace);

            throw new CommandException("An unexpected exception occurred while executing the command " + label, exceptionToSend.getCause() != null ? exceptionToSend.getCause() : exceptionToSend);
        });

        getErrorHandler().setFallbackHandler((namespace, e) -> {
            String label = namespace.getObject(String.class, "label");

            throw new CommandException("An unexpected exception occurred while executing the command " + label, e.getCause() != null ? e.getCause() : e);
        });
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
    public ErrorHandler getErrorHandler() {
        return commandManager.getErrorHandler();
    }

    @Override
    public void setErrorHandler(ErrorHandler errorHandler) {
        commandManager.setErrorHandler(errorHandler);
    }

    @Override
    public FallbackCommandModifiers getCommandModifiers() {
        return commandManager.getCommandModifiers();
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
