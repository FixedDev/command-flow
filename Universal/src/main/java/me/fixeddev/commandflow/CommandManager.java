package me.fixeddev.commandflow;

import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.executor.Executor;
import me.fixeddev.commandflow.exception.CommandException;
import me.fixeddev.commandflow.input.InputTokenizer;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Manages the command register and command execution.
 * In that way is a CommandRegistry and a CommandDispatcher at the same time, it also manages the parsing phase of the execution of the command
 * And manages the suggestions for the commands, used on Tab Completing on things like CLI applications or Bukkit
 *
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
     * Registers the specified {@link List} of {@link Command} into the internal command map doing a for loop and calling {@link CommandManager#registerCommand(Command)} for every command
     * @see CommandManager#registerCommand(Command)  
     * 
     * @param commandList The commands to register
     * @throws IllegalArgumentException If the main name of any command is already registered
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
     * @see CommandManager#unregisterCommand(Command) 
     * 
     * @param commands The {@linkplain Command} to unregister
     */
    void unregisterCommands(List<Command> commands);

    /**
     * Unregisters all the registered commands on this command manager
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

    /**
     * 
     * @return
     */
    Executor getExecutor();

    void setExecutor(Executor executor);

    /**
     * Searches a command with the specified name or alias and wraps it in an {@link Optional} instance
     * This operation is case insensitive
     *
     * @param commandName The command name or alias to check against
     * @return An {@link Optional} Command, absent if a command with that name or alias couldn't be found
     */
    Optional<Command> getCommand(String commandName);

    /**
     * Parses the command and creates a {@link CommandContext} instance used to execute the command.
     * <p>
     * If the executed {@link Command}'s {@link me.fixeddev.commandflow.command.Action} returns a false value then this method gets the usage for the executed Command
     *
     * @param accessor  The {@link Namespace} used to inject things into the Command parsing/execution phase
     * @param arguments A {@link List} of arguments including the command used to parse the actual command used and the parameters of that command
     * @return A boolean indicating if a command was executed or not
     * @throws CommandException      If the execution phase of the command fails for any reason
     */
    boolean execute(Namespace accessor, List<String> arguments) throws CommandException;

    /**
     * Partially parses the specified {@link List} of arguments and gets the suggestion for the last argument
     *
     * @param accessor  The {@link Namespace} used to inject objects
     * @param arguments A {@link List} of arguments including the command used to parse the actual command used and the parameters of that command
     * @return A {@link List} of {@link String} representing the suggestions for the next argument, empty if the player doesn't has permissions
     */
    List<String> getSuggestions(Namespace accessor, List<String> arguments);

    /**
     * Converts the specified line into a {@link List} of Strings using the {@link InputTokenizer} returned by {@link CommandManager#getInputTokenizer()}
     * and executes the {@linkplain CommandManager#execute(Namespace, List)} method
     *
     * @param accessor The {@link Namespace} used to inject things into the Command parsing/execution phase
     * @param line     A String representing the command line to tokenize
     * @return A boolean indicating if a command was executed or not
     * @throws CommandException      If the execution phase of the command fails for any reason
     * @see CommandManager#execute(Namespace, List)
     */
    boolean execute(Namespace accessor, String line) throws CommandException;

    /**
     * Converts the specified line into a {@link List} of Strings using the {@link InputTokenizer} returned by {@link CommandManager#getInputTokenizer()}
     * and executes the {@linkplain CommandManager#getSuggestions(Namespace, List)} method
     *
     * @param accessor The {@link Namespace} used to inject objects
     * @param line     A String representing the command line to tokenize
     * @return A {@link List} of {@link String} representing the suggestions for the next argument, empty if the player doesn't has permissions
     * @see CommandManager#getSuggestions(Namespace, List)
     */
    List<String> getSuggestions(Namespace accessor, String line);

}
