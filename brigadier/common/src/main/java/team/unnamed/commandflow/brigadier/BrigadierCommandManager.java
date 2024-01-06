package team.unnamed.commandflow.brigadier;

import com.mojang.brigadier.tree.LiteralCommandNode;
import team.unnamed.commandflow.Authorizer;
import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.CommandManager;
import team.unnamed.commandflow.ErrorHandler;
import team.unnamed.commandflow.Namespace;
import team.unnamed.commandflow.ParseResult;
import team.unnamed.commandflow.command.Command;
import team.unnamed.commandflow.command.modifiers.FallbackCommandModifiers;
import team.unnamed.commandflow.exception.CommandException;
import team.unnamed.commandflow.executor.Executor;
import team.unnamed.commandflow.input.InputTokenizer;
import team.unnamed.commandflow.translator.Translator;
import team.unnamed.commandflow.usage.UsageBuilder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

public class BrigadierCommandManager<T, S> implements CommandManager {
    private final CommandManager delegate;
    private final CommandBrigadierConverter<T, S> commandBrigadierConverter;
    private final BiConsumer<List<LiteralCommandNode<T>>, Command> onRegister;
    private final BiConsumer<List<LiteralCommandNode<T>>, Command> onUnregister;

    private final Map<Command, List<LiteralCommandNode<T>>> brigadierNodes;

    public BrigadierCommandManager(CommandManager delegate,
                                   CommandBrigadierConverter<T, S> commandBrigadierConverter,
                                   BiConsumer<List<LiteralCommandNode<T>>, Command> onRegister,
                                   BiConsumer<List<LiteralCommandNode<T>>, Command> onUnregister
    ) {
        this.delegate = delegate;
        this.commandBrigadierConverter = commandBrigadierConverter;
        this.onRegister = onRegister;
        this.onUnregister = onUnregister;

        brigadierNodes = new HashMap<>();
    }

    public Map<Command, List<LiteralCommandNode<T>>> getBrigadierNodes() {
        return brigadierNodes;
    }

    @Override
    public void registerCommand(Command command) {
        delegate.registerCommand(command);

        List<LiteralCommandNode<T>> nodes = commandBrigadierConverter.getBrigadierCommand(command);
        brigadierNodes.put(command, nodes);
        onRegister.accept(nodes, command);
    }

    @Override
    public void registerCommand(String label, Command command) {
        delegate.registerCommand(label, command);

        List<LiteralCommandNode<T>> nodes = commandBrigadierConverter.getBrigadierCommand(command);
        brigadierNodes.put(command, nodes);
        onRegister.accept(nodes, command);
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
        List<LiteralCommandNode<T>> nodes = brigadierNodes.get(command);

        if (nodes == null) {
            return;
        }

        onUnregister.accept(nodes, command);
    }

    @Override
    public void unregisterCommands(List<Command> commands) {
        for (Command command : commands) {
            unregisterCommand(command);
        }
    }

    @Override
    public void unregisterAll() {
        Set<Command> commands = new HashSet<>(brigadierNodes.keySet());

        for (Command command : commands) {
            unregisterCommand(command);
        }
    }

    @Override
    public Set<Command> getCommands() {
        return delegate.getCommands();
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
