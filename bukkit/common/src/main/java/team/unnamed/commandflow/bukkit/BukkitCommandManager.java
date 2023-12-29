package team.unnamed.commandflow.bukkit;

import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import team.unnamed.commandflow.Authorizer;
import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.CommandManager;
import team.unnamed.commandflow.ErrorHandler;
import team.unnamed.commandflow.Namespace;
import team.unnamed.commandflow.ParseResult;
import team.unnamed.commandflow.bukkit.sender.DefaultMessageSender;
import team.unnamed.commandflow.bukkit.sender.MessageSender;
import team.unnamed.commandflow.command.Command;
import team.unnamed.commandflow.command.modifiers.FallbackCommandModifiers;
import team.unnamed.commandflow.exception.ArgumentException;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static team.unnamed.commandflow.bukkit.BukkitCommonConstants.COMMAND_MANAGER_NAMESPACE;

public abstract class BukkitCommandManager implements CommandManager {
    protected CommandManager manager;
    private MessageSender messageSender;

    public BukkitCommandManager(CommandManager delegate) {
        this.manager = delegate;
        this.messageSender = new DefaultMessageSender();

        this.getErrorHandler().addExceptionHandler(CommandUsage.class, (namespace, ex) -> {
            CommandException exceptionToSend = ex;
            if (ex.getCause() instanceof ArgumentParseException) {
                exceptionToSend = (ArgumentParseException) ex.getCause();
            }

            sendMessageToSender(exceptionToSend, namespace);

            return true;
        });

        ErrorHandler.ErrorConsumer<ArgumentException> commonArgumentExceptionConsumer = (namespace, ex) -> {
            sendMessageToSender(ex, namespace);

            return false;
        };

        this.getErrorHandler().addExceptionHandler(InvalidSubCommandException.class, commonArgumentExceptionConsumer);
        this.getErrorHandler().addExceptionHandler(ArgumentParseException.class, commonArgumentExceptionConsumer);
        this.getErrorHandler().addExceptionHandler(NoMoreArgumentsException.class, commonArgumentExceptionConsumer);
        this.getErrorHandler().addExceptionHandler(NoPermissionsException.class, (namespace, throwable) -> {
            sendMessageToSender(throwable, namespace);

            return true;
        });

        this.getErrorHandler().addExceptionHandler(CommandException.class, (namespace, throwable) -> {
            Throwable exceptionToSend = throwable;

            String throwableMessage = throwable.getMessage();

            if ((throwableMessage != null && throwableMessage.equals("Internal error.")) || throwable.getCause() instanceof CommandException) {
                exceptionToSend = throwable.getCause();
            }

            sendMessageToSender(throwable, namespace);
            String label = namespace.getObject(String.class, "label");

            throw new org.bukkit.command.CommandException("An unexpected exception occurred while executing the command " + label, exceptionToSend);
        });


        this.getErrorHandler().setFallbackHandler((namespace, throwable) -> {
            String label = namespace.getObject(String.class, "label");

            throw new org.bukkit.command.CommandException("An unexpected exception occurred while executing the command " + label, throwable);
        });
    }

    protected abstract void _register(Command command);

    protected abstract void _unregister(Command command);

    public void registerCommand(Command command) {
        manager.registerCommand(command);

        _register(command);
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

        _unregister(command);
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
    public ErrorHandler getErrorHandler() {
        return manager.getErrorHandler();
    }

    @Override
    public void setErrorHandler(ErrorHandler errorHandler) {
        manager.setErrorHandler(errorHandler);
    }

    @Override
    public FallbackCommandModifiers getCommandModifiers() {
        return manager.getCommandModifiers();
    }

    public MessageSender getMessageSender() {
        return this.messageSender;
    }

    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Override
    public Optional<Command> getCommand(String commandName) {
        return manager.getCommand(commandName);
    }

    @Override
    public boolean execute(Namespace accessor, List<String> arguments) throws CommandException {
        accessor.setObject(BukkitCommandManager.class, COMMAND_MANAGER_NAMESPACE, this);

        return manager.execute(accessor, arguments);
    }

    @Override
    public List<String> getSuggestions(Namespace accessor, List<String> arguments) {
        return manager.getSuggestions(accessor, arguments);
    }

    @Override
    public boolean execute(Namespace accessor, String line) throws CommandException {
        accessor.setObject(BukkitCommandManager.class, COMMAND_MANAGER_NAMESPACE, this);

        return manager.execute(accessor, line);
    }

    @Override
    public List<String> getSuggestions(Namespace accessor, String line) {
        accessor.setObject(BukkitCommandManager.class, COMMAND_MANAGER_NAMESPACE, this);

        return manager.getSuggestions(accessor, line);
    }

    @Override
    public boolean execute(CommandContext commandContext) throws CommandException {
        commandContext.setObject(BukkitCommandManager.class, COMMAND_MANAGER_NAMESPACE, this);

        return manager.execute(commandContext);
    }

    @Override
    public ParseResult parse(Namespace accessor, List<String> arguments) throws CommandException {
        accessor.setObject(BukkitCommandManager.class, COMMAND_MANAGER_NAMESPACE, this);

        return manager.parse(accessor, arguments);
    }

    @Override
    public ParseResult parse(Namespace accessor, String line) throws CommandException {
        accessor.setObject(BukkitCommandManager.class, COMMAND_MANAGER_NAMESPACE, this);

        return manager.parse(accessor, line);
    }

    public static void sendMessageToSender(CommandException exception, Namespace namespace) {
        BukkitCommandManager commandManager = namespace.getObject(BukkitCommandManager.class, COMMAND_MANAGER_NAMESPACE); // should be, lol
        CommandSender sender = namespace.getObject(CommandSender.class, BukkitCommonConstants.SENDER_NAMESPACE);

        Component component = exception.getMessageComponent();
        Component translatedComponent = commandManager.getTranslator().translate(component, namespace);

        commandManager.getMessageSender().sendMessage(sender, translatedComponent);
    }
}
