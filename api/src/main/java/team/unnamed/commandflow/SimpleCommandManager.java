package team.unnamed.commandflow;

import team.unnamed.commandflow.command.Command;
import team.unnamed.commandflow.command.modifiers.CommandModifiers;
import team.unnamed.commandflow.command.modifiers.FallbackCommandModifiers;
import team.unnamed.commandflow.command.modifiers.ModifierPhase;
import team.unnamed.commandflow.exception.ArgumentException;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.exception.CommandException;
import team.unnamed.commandflow.exception.CommandUsage;
import team.unnamed.commandflow.exception.NoPermissionsException;
import team.unnamed.commandflow.exception.StopParseException;
import team.unnamed.commandflow.executor.DefaultExecutor;
import team.unnamed.commandflow.executor.Executor;
import team.unnamed.commandflow.input.InputTokenizer;
import team.unnamed.commandflow.input.StringSpaceTokenizer;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.stack.ArgumentStack;
import team.unnamed.commandflow.stack.SimpleArgumentStack;
import team.unnamed.commandflow.translator.DefaultMapTranslationProvider;
import team.unnamed.commandflow.translator.DefaultTranslator;
import team.unnamed.commandflow.translator.Translator;
import team.unnamed.commandflow.usage.DefaultUsageBuilder;
import team.unnamed.commandflow.usage.UsageBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * The default implementation for {@link CommandManager} using a HashMap for the internal commandMap
 * <p>
 * This class is not threadsafe, we can't ensure that registering/executing commands on more than 1 thread concurrently works correctly
 */
public class SimpleCommandManager implements CommandManager {

    private final Map<String, Command> commandMap;

    private Authorizer authorizer;
    private InputTokenizer tokenizer;
    private Executor executor;
    private Translator translator;
    private UsageBuilder usageBuilder;
    private ErrorHandler errorHandler;

    private final FallbackCommandModifiers fallbackCommandModifiers;

    public SimpleCommandManager(Authorizer authorizer) {
        this.authorizer = authorizer;
        commandMap = new HashMap<>();
        tokenizer = new StringSpaceTokenizer();

        executor = new DefaultExecutor();
        translator = new DefaultTranslator(new DefaultMapTranslationProvider());
        usageBuilder = new DefaultUsageBuilder();

        fallbackCommandModifiers = new FallbackCommandModifiers();

        errorHandler = new SimpleErrorHandler();

        // do nothing, just ignore it.
        errorHandler.addExceptionHandler(StopParseException.class, (namespace, throwable) -> true);
    }

    public SimpleCommandManager() {
        this((namespace, permission) -> true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerCommand(Command command) {
        if (commandMap.containsKey(command.getName())) {
            throw new IllegalArgumentException("A command with the name " + command.getName() + " is already registered!");
        }

        commandMap.put(command.getName().toLowerCase(), command);

        command.getAliases().forEach(alias -> commandMap.putIfAbsent(alias.toLowerCase(), command));
    }

    @Override
    public void registerCommand(String label, Command command) {
        commandMap.putIfAbsent(label.toLowerCase(), command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerCommands(List<Command> commandList) {
        for (Command command : commandList) {
            registerCommand(command);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterCommand(Command command) {
        commandMap.remove(command.getName());

        for (String alias : command.getAliases()) {
            commandMap.remove(alias);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterCommands(List<Command> commands) {
        for (Command command : commands) {
            unregisterCommand(command);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterAll() {
        Set<Command> commands = new HashSet<>(commandMap.values());

        for (Command command : commands) {
            unregisterCommand(command);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Command> getCommands() {
        return new HashSet<>(commandMap.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists(String commandName) {
        return commandMap.containsKey(commandName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Authorizer getAuthorizer() {
        return authorizer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAuthorizer(Authorizer authorizer) {
        if (authorizer == null) {
            throw new IllegalArgumentException("Trying to set a null authorizer!");
        }

        this.authorizer = authorizer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputTokenizer getInputTokenizer() {
        return tokenizer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInputTokenizer(InputTokenizer tokenizer) {
        if (tokenizer == null) {
            throw new IllegalArgumentException("Trying to set a null input tokenizer!");
        }

        this.tokenizer = tokenizer;
    }

    @Override
    public Executor getExecutor() {
        return executor;
    }

    @Override
    public void setExecutor(Executor executor) {
        if (executor == null) {
            throw new IllegalArgumentException("Trying to set a null Executor!");
        }

        this.executor = executor;
    }

    @Override
    public Translator getTranslator() {
        return translator;
    }

    @Override
    public void setTranslator(Translator translator) {
        if (translator == null) {
            throw new IllegalArgumentException("Trying to set a null Translator!");
        }

        this.translator = translator;
    }

    @Override
    public UsageBuilder getUsageBuilder() {
        return usageBuilder;
    }

    @Override
    public void setUsageBuilder(UsageBuilder usageBuilder) {
        if (usageBuilder == null) {
            throw new IllegalArgumentException("Trying to set a null UsageBuilder!");
        }

        this.usageBuilder = usageBuilder;
    }

    @Override
    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    @Override
    public void setErrorHandler(ErrorHandler errorHandler) {
        if (this.errorHandler == null) {
            throw new IllegalArgumentException("Trying to set a null ErrorHandler!");
        }

        this.errorHandler = errorHandler;
    }

    @Override
    public FallbackCommandModifiers getCommandModifiers() {
        return fallbackCommandModifiers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Command> getCommand(String commandName) {
        return Optional.ofNullable(commandMap.get(commandName.toLowerCase()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean execute(CommandContext commandContext) throws CommandException {
        if (commandContext == null) {
            throw new IllegalArgumentException("The CommandContext can't be null!");
        }

        return executor.execute(commandContext, getUsageBuilder());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean execute(Namespace accessor, List<String> arguments) throws CommandException {
        ParseResult result = parse(accessor, arguments);

        Optional<CommandException> optionalException = result.getException();
        Optional<CommandContext> optionalContext = result.getContext();

        if (optionalException.isPresent()) {
            CommandException exception = optionalException.get();

            if (exception instanceof ArgumentException && !(exception instanceof ArgumentParseException)) {
                // The context is there if the exception is an ArgumentException, ignore the warning
                CommandContext commandContext = optionalContext.get();
                ArgumentException e = (ArgumentException) exception;

                exception = new CommandUsage(usageBuilder.getUsage(commandContext))
                        .setCommand(commandContext.getCommand());
                exception.initCause(e);
            }

            try {
                return errorHandler.handleException(accessor, exception);
            } catch (Throwable ex) {
                throwOrWrap(ex);
            }
        }

        if (!optionalContext.isPresent()) {
            return false;
        }

        CommandContext commandContext = optionalContext.get();

        CommandModifiers modifiers = commandContext.getCommand().getModifiers();

        if (!modifiers.hasModifiers(ModifierPhase.PRE_EXECUTE)) {
            if (!fallbackCommandModifiers.callModifiers(ModifierPhase.PRE_EXECUTE, commandContext, null)) {
                return false;
            }
        } else {
            if (!modifiers.callModifiers(ModifierPhase.PRE_EXECUTE, commandContext, null)) {
                return false;
            }
        }


        try {
            return executor.execute(commandContext, getUsageBuilder());
        } catch (Throwable e) {
            try {
                return errorHandler.handleException(accessor, e);
            } catch (Throwable ex) {
                throwOrWrap(ex);
            }

            return false;
        } finally {
            if (!modifiers.hasModifiers(ModifierPhase.POST_EXECUTE)) {
                fallbackCommandModifiers.callModifiers(ModifierPhase.POST_EXECUTE, commandContext, null);
            } else {
                commandContext.getCommand().getModifiers().callModifiers(ModifierPhase.POST_EXECUTE, commandContext, null);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean execute(Namespace accessor, String line) throws CommandException {
        return execute(accessor, tokenizer.tokenize(line));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getSuggestions(Namespace accessor, List<String> arguments) {
        if (arguments == null || arguments.isEmpty()) {
            return Collections.emptyList();
        }

        Optional<Command> optionalCommand = getCommand(arguments.get(0));

        if (!optionalCommand.isPresent()) {
            return Collections.emptyList();
        }

        arguments.remove(0);

        Command command = optionalCommand.get();

        if (!authorizer.isAuthorized(accessor, command.getPermission())) {
            return Collections.emptyList();
        }

        CommandContext commandContext = new SimpleCommandContext(accessor, arguments);
        commandContext.setCommand(command, command.getName());

        accessor.setObject(CommandManager.class, "commandManager", this);

        ArgumentStack stack = new SimpleArgumentStack(arguments);

        List<String> suggestions = command.getPart().getSuggestions(commandContext, stack);

        return suggestions == null ? Collections.emptyList() : suggestions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getSuggestions(Namespace accessor, String line) {
        return getSuggestions(accessor, tokenizer.tokenize(line));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParseResult parse(Namespace accessor, String line) {
        return parse(accessor, tokenizer.tokenize(line));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParseResult parse(Namespace accessor, List<String> arguments) {
        if (arguments == null || arguments.isEmpty()) {
            return empty();
        }

        Optional<Command> optionalCommand = getCommand(arguments.get(0));

        if (!optionalCommand.isPresent()) {
            return empty();
        }

        accessor.setObject(CommandManager.class, "commandManager", this);

        ArgumentStack stack = new SimpleArgumentStack(arguments);

        String label = stack.next();
        Command command = optionalCommand.get();

        if (!authorizer.isAuthorized(accessor, command.getPermission())) {
            NoPermissionsException exception = new NoPermissionsException(command.getPermissionMessage());
            exception.setCommand(command);

            return ofError(exception);
        }

        CommandContext commandContext = new SimpleCommandContext(accessor, arguments);
        commandContext.setCommand(command, label);

        CommandModifiers modifiers = command.getModifiers();

        if (!modifiers.hasModifiers(ModifierPhase.PRE_PARSE)) {
            if (!fallbackCommandModifiers.callModifiers(ModifierPhase.PRE_PARSE, commandContext, stack)) {
                // we just want to stop here if the pre-parse modifiers return false
                return new ParseResultImpl();
            }
        } else {
            if (!modifiers.callModifiers(ModifierPhase.PRE_PARSE, commandContext, stack)) {
                // we just want to stop here if the pre-parse modifiers return false
                return new ParseResultImpl();
            }
        }

        CommandPart part = command.getPart();

        try {
            part.parse(commandContext, stack, null);
        } catch (ArgumentParseException e) {
            return ofError(commandContext, e);
        } catch (ArgumentException e) {
            CommandUsage usage = new CommandUsage(usageBuilder.getUsage(commandContext));
            usage.setCommand(commandContext.getCommand());

            usage.initCause(e);

            return ofError(commandContext, e);
        } catch (CommandException e) {
            return ofError(commandContext, e);
        }

        return ofSuccess(commandContext);
    }

    private void throwOrWrap(Throwable throwable) throws CommandException {
        if (throwable instanceof CommandException) {
            throw (CommandException) throwable;
        }

        throw new CommandException(throwable);
    }

    private ParseResult empty() {
        return new ParseResultImpl();
    }

    private ParseResult ofSuccess(CommandContext commandContext) {
        return new ParseResultImpl(commandContext);
    }

    private ParseResult ofError(CommandException exception) {
        return new ParseResultImpl(exception);
    }

    private ParseResult ofError(CommandContext commandContext, CommandException exception) {
        return new ParseResultImpl(commandContext, exception);
    }

    protected static class ParseResultImpl implements ParseResult {

        private CommandContext commandContext;
        private CommandException exception;

        public ParseResultImpl(CommandContext commandContext, CommandException exception) {
            this.commandContext = commandContext;
            this.exception = exception;
        }

        public ParseResultImpl(CommandContext commandContext) {
            this.commandContext = commandContext;
        }

        public ParseResultImpl(CommandException exception) {
            this.exception = exception;
        }

        public ParseResultImpl() {
        }

        @Override
        public Optional<CommandContext> getContext() {
            return Optional.ofNullable(commandContext);
        }

        @Override
        public Optional<CommandException> getException() {
            return Optional.ofNullable(exception);
        }

    }

}
