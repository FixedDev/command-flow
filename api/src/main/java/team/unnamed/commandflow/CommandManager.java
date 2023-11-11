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
package team.unnamed.commandflow;

import team.unnamed.commandflow.command.Command;
import team.unnamed.commandflow.command.modifiers.FallbackCommandModifiers;
import team.unnamed.commandflow.executor.Executor;
import team.unnamed.commandflow.exception.CommandException;
import team.unnamed.commandflow.input.InputTokenizer;
import team.unnamed.commandflow.translator.Translator;
import team.unnamed.commandflow.usage.UsageBuilder;
import team.unnamed.commandflow.command.Action;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Manages the command register and command execution.
 * In that way is a CommandRegistry and a CommandDispatcher at the same time, it also manages the parsing phase of the execution of the command
 * And manages the suggestions for the commands, used on Tab Completing on things like CLI applications or Bukkit
 */
public interface CommandManager {

    /**
     * Registers the specified {@link Command} into the internal command map
     * Also registers every alias of the command as separate commands with the same value
     *
     * @param command The command to register
     * @throws IllegalArgumentException If the main name is already registered
     */
    void registerCommand(Command command);

    /**
     * Registers the specified {@link Command} into the internal command map with the specified label
     * <p>
     * This method opposed to {@link CommandManager#registerCommand(Command)} doesn't has a fail-fast behaviour, just ignores the registered label
     * if already registered.
     *
     * @param command The command to register
     * @param label   The label to register the command with
     */
    void registerCommand(String label, Command command);

    /**
     * Registers the specified {@link List} of {@link Command} into the internal command map doing a for loop and calling {@link CommandManager#registerCommand(Command)} for every command
     *
     * @param commandList The commands to register
     * @throws IllegalArgumentException If the main name of any command is already registered
     * @see CommandManager#registerCommand(Command)
     */
    void registerCommands(List<Command> commandList);

    /**
     * Unregisters the specified {@link Command} from the internal command map including all of it's aliases
     *
     * @param command The {@linkplain Command} to unregister
     */
    void unregisterCommand(Command command);

    /**
     * Unregisters the specified {@link List} of {@link Command} from the internal command map doing a for loop and calling {@link CommandManager#unregisterCommand(Command)} for every command
     *
     * @param commands The {@linkplain Command} to unregister
     * @see CommandManager#unregisterCommand(Command)
     */
    void unregisterCommands(List<Command> commands);

    /**
     * Unregisters all the registered commands on this command manager
     *
     * @see CommandManager#unregisterCommand(Command)
     */
    void unregisterAll();

    /**
     * A {@linkplain Set} of all the registered {@link Command} instances on this command manager
     *
     * @return An immutable set representing the registered commands
     */
    Set<Command> getCommands();

    /**
     * Checks if the specified {@link String} is the name or alias of a registered command
     * The name checking is case insensitive
     *
     * @param commandName The command name or alias to check against
     * @return if a command with the specified {@code commandName} as main name or alias exists
     */
    boolean exists(String commandName);

    /**
     * The {@link Authorizer} used to check if the actual context of the command is authorized to execute this command
     *
     * @return The {@link Authorizer} used on this CommandManager instance
     */
    Authorizer getAuthorizer();

    /**
     * Changes the {@link Authorizer} used on this instance
     *
     * @param authorizer A non null instance of a {@link Authorizer}
     * @throws IllegalArgumentException If the specified authorizer is null
     */
    void setAuthorizer(Authorizer authorizer);

    /**
     * The {@link InputTokenizer} used by this instance on the methods:
     * {@link CommandManager#execute(Namespace, String)}
     * {@link CommandManager#getSuggestions(Namespace, String)}
     * to tokenize the command line into a {@link List} of String
     *
     * @return The {@link InputTokenizer} used on this CommandManager instance
     */
    InputTokenizer getInputTokenizer();

    /**
     * Changes the {@link InputTokenizer} used on this instance
     *
     * @param tokenizer A non null instance of an {@link InputTokenizer}
     * @throws IllegalArgumentException If the specified tokenizer is null
     */
    void setInputTokenizer(InputTokenizer tokenizer);

    Executor getExecutor();

    void setExecutor(Executor executor);

    Translator getTranslator();

    void setTranslator(Translator translator);

    UsageBuilder getUsageBuilder();

    void setUsageBuilder(UsageBuilder usageBuilder);

    ErrorHandler getErrorHandler();

    void setErrorHandler(ErrorHandler errorHandler);

    FallbackCommandModifiers getCommandModifiers();

    /**
     * Searches a command with the specified name or alias and wraps it in an {@link Optional} instance
     * This operation is case insensitive
     *
     * @param commandName The command name or alias to check against
     * @return An {@link Optional} Command, absent if a command with that name or alias couldn't be found
     */
    Optional<Command> getCommand(String commandName);

    /**
     * Executes a command based on the provided {@link CommandContext}.
     * <p>
     * If the executed {@link Command}'s {@link Action} returns a false value then this method gets the usage for the executed Command
     *
     * @param commandContext The {@link CommandContext} used to execute the command.
     * @return A boolean indicating if a command was executed or not
     * @throws CommandException If the execution phase of the command fails for any reason
     */
    boolean execute(CommandContext commandContext) throws CommandException;

    /**
     * Calls {@link CommandManager#parse(Namespace, List)} and executes the command with the {@link CommandContext} returned by the method.
     * <p>
     * If the executed {@link Command}'s {@link Action} returns a false value then this method gets the usage for the executed Command
     *
     * @param accessor  The {@link Namespace} used to inject things into the Command parsing/execution phase
     * @param arguments A {@link List} of arguments including the command used to parse the actual command used and the parameters of that command
     * @return A boolean indicating if a command was executed or not
     * @throws CommandException If the execution phase of the command fails for any reason
     */
    boolean execute(Namespace accessor, List<String> arguments) throws CommandException;

    /**
     * Parses the specified list of arguments with the specified {@link Namespace} into a CommandContext.
     * As a side note, the implementation also injects this instance into the {@link Namespace} with the name "commandManager"
     *
     * @param accessor  The {@link Namespace} used to inject things into the Command parsing/execution phase
     * @param arguments A {@link List} of arguments including the command used to parse the actual command used and the parameters of that command
     * @return An Optional CommandContext instance, absent if the arguments are not present or the command doesn't exists
     * @throws CommandException If the parsing fails for any reason, included but not limited to not having permissions.
     */
    ParseResult parse(Namespace accessor, List<String> arguments) throws CommandException;

    /**
     * Partially parses the specified {@link List} of arguments and gets the suggestion for the last argument
     *
     * @param accessor  The {@link Namespace} used to inject objects
     * @param arguments A {@link List} of arguments including the command used to parse the actual command used and the parameters of that command
     * @return A non-null {@link List} of {@link String} representing the suggestions for the next argument, empty if the player doesn't has permissions
     */
    List<String> getSuggestions(Namespace accessor, List<String> arguments);

    /**
     * Converts the specified line into a {@link List} of Strings using the {@link InputTokenizer} returned by {@link CommandManager#getInputTokenizer()}
     * and executes the {@linkplain CommandManager#execute(Namespace, List)} method
     *
     * @param accessor The {@link Namespace} used to inject things into the Command parsing/execution phase
     * @param line     A String representing the command line to tokenize
     * @return A boolean indicating if a command was executed or not
     * @throws CommandException If the execution phase of the command fails for any reason
     * @see CommandManager#execute(Namespace, List)
     */
    boolean execute(Namespace accessor, String line) throws CommandException;

    /**
     * Converts the specified line into a {@link List} of Strings using the {@link InputTokenizer} returned by {@link CommandManager#getInputTokenizer()}
     * and executes the {@linkplain CommandManager#getSuggestions(Namespace, List)} method
     *
     * @param accessor The {@link Namespace} used to inject objects
     * @param line     A String representing the command line to tokenize
     * @return A non-null {@link List} of {@link String} representing the suggestions for the next argument, empty if the player doesn't has permissions
     * @see CommandManager#getSuggestions(Namespace, List)
     */
    List<String> getSuggestions(Namespace accessor, String line);

    /**
     * Converts the specified line into a {@link List} of Strings using the {@link InputTokenizer} returned by {@link CommandManager#getInputTokenizer()}
     * and executes the {@linkplain CommandManager#parse(Namespace, List)} method.
     * <p>
     * Parses the specified list of arguments with the specified {@link Namespace} into a CommandContext.
     * As a side note, the implementation also injects this instance into the {@link Namespace} with the name "commandManager"
     *
     * @param accessor The {@link Namespace} used to inject things into the Command parsing/execution phase
     * @param line     A String representing the command line to tokenize
     * @return An Optional CommandContext instance, absent if the arguments are not present or the command doesn't exists
     * @throws CommandException If the parsing fails for any reason, included but not limited to not having permissions.
     * @see CommandManager#parse(Namespace, List)
     */
    ParseResult parse(Namespace accessor, String line) throws CommandException;

}
