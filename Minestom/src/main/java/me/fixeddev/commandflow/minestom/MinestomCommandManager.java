package me.fixeddev.commandflow.minestom;

import me.fixeddev.commandflow.Authorizer;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.ErrorHandler;
import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.ParseResult;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.command.modifiers.FallbackCommandModifiers;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.exception.CommandException;
import me.fixeddev.commandflow.exception.CommandUsage;
import me.fixeddev.commandflow.exception.InvalidSubCommandException;
import me.fixeddev.commandflow.exception.NoMoreArgumentsException;
import me.fixeddev.commandflow.exception.NoPermissionsException;
import me.fixeddev.commandflow.executor.Executor;
import me.fixeddev.commandflow.input.InputTokenizer;
import me.fixeddev.commandflow.translator.Translator;
import me.fixeddev.commandflow.usage.UsageBuilder;
import net.minestom.server.MinecraftServer;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class MinestomCommandManager implements CommandManager {

    public static final String SENDER_NAMESPACE = "SENDER";

    private final CommandManager delegate;

    public MinestomCommandManager(CommandManager delegate) {
        this.delegate = delegate;

        getTranslator().setProvider(new MinestomDefaultTranslationProvider());
        setAuthorizer(new MinestomAuthorizer());

        ErrorHandler.ErrorConsumer<CommandException> commonHandler = (namespace, e) -> {
            MinestomCommandWrapper.sendMessageToSender(e, namespace);

            return true;
        };

        this.getErrorHandler().addExceptionHandler(CommandUsage.class, (namespace, ex) -> {
            CommandException exceptionToSend = ex;
            if (ex.getCause() instanceof ArgumentParseException) {
                exceptionToSend = (ArgumentParseException) ex.getCause();
            }

            MinestomCommandWrapper.sendMessageToSender(exceptionToSend, namespace);

            return true;
        });

        getErrorHandler().addExceptionHandler(InvalidSubCommandException.class, commonHandler);
        getErrorHandler().addExceptionHandler(ArgumentParseException.class, commonHandler);
        getErrorHandler().addExceptionHandler(NoMoreArgumentsException.class, commonHandler);
        getErrorHandler().addExceptionHandler(NoPermissionsException.class, commonHandler);

        getErrorHandler().setFallbackHandler((namespace, throwable) -> {
            String label = namespace.getObject(String.class, "label");

            throw new CommandException("An unexpected exception occurred while executing the command " + label, throwable);
        });

        getErrorHandler().addExceptionHandler(CommandException.class, (namespace, e) -> {
            CommandException exceptionToSend = e;

            if (e.getCause() instanceof CommandException) {
                exceptionToSend = (CommandException) e.getCause();
            }

            MinestomCommandWrapper.sendMessageToSender(e, namespace);

            throw new CommandException("An unexpected exception occurred while executing the command " + e.getCommand().getName(), exceptionToSend);
        });
    }

    @Override
    public void registerCommand(Command command) {
        MinestomCommandWrapper commandWrapper = new MinestomCommandWrapper(
                command,
                this,
                command.getName(),
                command.getAliases().toArray(new String[0])
        );

        MinecraftServer.getCommandManager().register(commandWrapper);
        delegate.registerCommand(command);
    }

    @Override
    public void registerCommand(String label, Command command) {
        delegate.registerCommand(label, command);
    }

    @Override
    public void registerCommands(List<Command> commandList) {
        for (Command command : commandList) {
            registerCommand(command);
        }
    }

    @Override
    public void unregisterCommand(Command command) {
        delegate.unregisterCommand(command);

        net.minestom.server.command.builder.Command minestomCommand = MinecraftServer.getCommandManager().getCommand(command.getName());
        if (minestomCommand instanceof MinestomCommandWrapper) {
            MinecraftServer.getCommandManager().unregister(minestomCommand);
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
        for (Command command : getCommands()) {
            unregisterCommand(command);
        }
    }

    @Override
    public Set<Command> getCommands() {
        return new HashSet<>(delegate.getCommands());
    }

    @Override
    public boolean exists(String commandName) {
        return delegate.exists(commandName);
    }

    @Override
    public Authorizer getAuthorizer() {
        return delegate.getAuthorizer();
    }

    @Override
    public void setAuthorizer(Authorizer authorizer) {
        delegate.setAuthorizer(authorizer);
    }

    @Override
    public InputTokenizer getInputTokenizer() {
        return delegate.getInputTokenizer();
    }

    @Override
    public void setInputTokenizer(InputTokenizer tokenizer) {
        delegate.setInputTokenizer(tokenizer);
    }

    @Override
    public Executor getExecutor() {
        return delegate.getExecutor();
    }

    @Override
    public void setExecutor(Executor executor) {
        delegate.setExecutor(executor);
    }

    @Override
    public Translator getTranslator() {
        return delegate.getTranslator();
    }

    @Override
    public void setTranslator(Translator translator) {
        delegate.setTranslator(translator);
    }

    @Override
    public UsageBuilder getUsageBuilder() {
        return delegate.getUsageBuilder();
    }

    @Override
    public void setUsageBuilder(UsageBuilder usageBuilder) {
        delegate.setUsageBuilder(usageBuilder);
    }

    @Override
    public ErrorHandler getErrorHandler() {
        return delegate.getErrorHandler();
    }

    @Override
    public void setErrorHandler(ErrorHandler errorHandler) {
        delegate.setErrorHandler(errorHandler);
    }

    @Override
    public FallbackCommandModifiers getCommandModifiers() {
        return delegate.getCommandModifiers();
    }

    @Override
    public Optional<Command> getCommand(String commandName) {
        return delegate.getCommand(commandName);
    }

    @Override
    public boolean execute(CommandContext commandContext) throws CommandException {
        return delegate.execute(commandContext);
    }

    @Override
    public boolean execute(Namespace accessor, List<String> arguments) throws CommandException {
        return delegate.execute(accessor, arguments);
    }

    @Override
    public ParseResult parse(Namespace accessor, List<String> arguments) throws CommandException {
        return delegate.parse(accessor, arguments);
    }

    @Override
    public List<String> getSuggestions(Namespace accessor, List<String> arguments) {
        return delegate.getSuggestions(accessor, arguments);
    }

    @Override
    public boolean execute(Namespace accessor, String line) throws CommandException {
        return delegate.execute(accessor, line);
    }

    @Override
    public List<String> getSuggestions(Namespace accessor, String line) {
        return delegate.getSuggestions(accessor, line);
    }

    @Override
    public ParseResult parse(Namespace accessor, String line) throws CommandException {
        return delegate.parse(accessor, line);
    }
}