/*
 * This file is part of commandflow, licensed under the MIT license
 *
 * Copyright (c) 2020-2023 Unnamed Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package team.unnamed.commandflow.velocity;

import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.proxy.ProxyServer;
import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.CommandManager;
import team.unnamed.commandflow.ErrorHandler;
import team.unnamed.commandflow.Namespace;
import team.unnamed.commandflow.SimpleCommandManager;
import team.unnamed.commandflow.Authorizer;
import team.unnamed.commandflow.ParseResult;
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

import java.util.*;

public class VelocityCommandManager implements CommandManager {

    public static final String SENDER_NAMESPACE = "SENDER";

    protected final ProxyServer proxyServer;
    protected final CommandManager commandManager;
    protected final Object plugin;

    protected final Map<String, VelocityCommandWrapper> wrapperMap;

    public VelocityCommandManager(ProxyServer proxyServer, Object plugin) {
        this(proxyServer, new SimpleCommandManager(), plugin);
    }

    public VelocityCommandManager(ProxyServer proxyServer, CommandManager commandManager, Object plugin) {
        this.proxyServer = proxyServer;
        this.commandManager = commandManager;
        this.plugin = plugin;

        wrapperMap = new HashMap<>();

        setAuthorizer(new VelocityAuthorizer());
        getTranslator().setProvider(new VelocityDefaultTranslationProvider());
        getTranslator().setConverterFunction(LegacyComponentSerializer.legacyAmpersand()::deserialize);

        getErrorHandler().addExceptionHandler(CommandUsage.class, (namespace, e) -> {
            CommandException exceptionToSend = e;
            if (e.getCause() instanceof ArgumentParseException) {
                exceptionToSend = (ArgumentParseException) e.getCause();
            }

            VelocityCommandWrapper.sendMessageToSender(exceptionToSend, namespace);
            return true;
        });


        ErrorHandler.ErrorConsumer<CommandException> commonHandler = (namespace, e) -> {
            VelocityCommandWrapper.sendMessageToSender(e, namespace);

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

            VelocityCommandWrapper.sendMessageToSender(e, namespace);

            throw new CommandException("An unexpected exception occurred while executing the command " + e.getCommand().getName(), exceptionToSend);
        });


        getErrorHandler().setFallbackHandler((namespace, throwable) -> {
            String label = namespace.getObject(String.class, "label");

            throw new CommandException("An unexpected exception occurred while executing the command " + label, throwable);
        });
    }

    @Override
    public void registerCommand(Command command) {
        commandManager.registerCommand(command);

        VelocityCommandWrapper velocityCommandWrapper = new VelocityCommandWrapper(command, commandManager, getTranslator());
        wrapperMap.put(command.getName(), velocityCommandWrapper);

        final CommandMeta commandMeta = proxyServer.getCommandManager().metaBuilder(command.getName())
                        .aliases(command.getAliases().toArray(new String[0]))
                        .plugin(plugin)
                        .build();

        proxyServer.getCommandManager().register(commandMeta, velocityCommandWrapper);
    }

    @Override
    public void registerCommand(String label, Command command) {
        commandManager.registerCommand(label,command);
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

        VelocityCommandWrapper velocityCommandWrapper = wrapperMap.get(command.getName());
        if (velocityCommandWrapper != null) {
            final CommandMeta commandMeta = proxyServer.getCommandManager().getCommandMeta(command.getName());
            proxyServer.getCommandManager().unregister(commandMeta);
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
}
