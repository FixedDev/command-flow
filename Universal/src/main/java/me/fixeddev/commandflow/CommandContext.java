package me.fixeddev.commandflow;

import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.part.CommandPart;

import java.util.List;
import java.util.Optional;

public interface CommandContext extends Namespace {

    /**
     * Copies this {@link CommandContext} into an immutable instance of {@link ContextSnapshot} copying all properties at the time of
     * creating the snapshot.
     *
     * @return An immutable {@link ContextSnapshot}
     */
    ContextSnapshot getSnapshot();

    /**
     * Applies a pre created {@link ContextSnapshot} into this {@link CommandContext} instance, effectively reverting this
     * instance into the values of the {@link ContextSnapshot}
     *
     * @param snapshot The {@link ContextSnapshot} to apply.
     */
    void applySnapshot(ContextSnapshot snapshot);

    /**
     * Changes the current context for the {@link Command} and adds the {@link Command} into the execution path and labels list.
     *
     * @param command The {@link Command} to set for this context.
     * @param label   The label of the {@link Command} to add into the label list.
     */
    void setCommand(Command command, String label);

    /**
     * Changes the current context for the {@link Command} and removes the last {@link Command} from the execution path and labels list.
     */
    void removeLastCommand();

    /**
     * The {@link Command} to execute
     *
     * @return A nullable {@link Command} instance if a command wasn't added yet.
     */
    default Command getCommand() {
        return getExecutionPath().get(getExecutionPath().size() - 1);
    }

    /**
     * The root {@link Command} which is the command that started the execution.
     *
     * @return A nullable {@link Command} instance representing the root command.
     */
    default Command getRootCommand() {
        return getExecutionPath().get(0);
    }

    /**
     * The execution path for the command, representing the commands/subcommands that were parsed to get the actual command(last value of the path)
     *
     * @return A list of {@link Command} instances
     */
    List<Command> getExecutionPath();

    /**
     * The raw arguments that were provided for the command including the command.
     *
     * @return A non null {@link List} of strings representing the arguments for the executed command.
     */
    List<String> getArguments();

    /**
     * The labels for the commands that were executed.
     *
     * @return The {@link List} of labels for the executed command/subcommands.
     */
    List<String> getLabels();

    /**
     * Searches in the list of {@link CommandPart} the parts with a specified name for the executed commands
     *
     * @param name The name of the {@link CommandPart} list to search for.
     * @return A {@link List} of {@link CommandPart} with the specified name.
     */
    List<CommandPart> getParts(String name);

    /**
     * Searches the first {@link CommandPart} in the {@link List} with a name.
     *
     * @param name The name of the {@link CommandPart} to search for.
     * @return A {@link CommandPart} with the specified name.
     * @see CommandContext#getParts(String)
     */
    default Optional<CommandPart> getPart(String name) {
        return getPart(name, 0);
    }

    /**
     * Searches the nth {@link CommandPart} in the {@link List} with a name.
     *
     * @param name  The name of the {@link CommandPart} to search for.
     * @param index The index in the {@link List} for {@link CommandPart} to search for.
     * @return A {@link CommandPart} with the specified name and index.
     * @see CommandContext#getParts(String)
     */
    default Optional<CommandPart> getPart(String name, int index) {
        List<CommandPart> parts = getParts(name);

        return Optional.ofNullable(index < parts.size() ? getParts(name).get(index) : null);
    }

    /**
     * Checks into the {@link List} of {@link CommandPart} instances if the specified {@link CommandPart} exists.
     *
     * @param part The {@link CommandPart} to search for.
     * @return A boolean representing if the specified {@link CommandPart} is contained in the current command.
     */
    boolean contains(CommandPart part);

    /**
     * Gets the raw string value for the specified {@link CommandPart}
     *
     * @param part The {@link CommandPart} to search with.
     * @return Raw {@link List} of string arguments for the specified {@link CommandPart}.
     */
   List<String> getRaw(CommandPart part);

    /**
     * Gets the converted values of the specified {@link CommandPart}
     *
     * @param part The {@link CommandPart} to search values for.
     * @param <V>  The type of the returned value.
     * @return The {@link List} of values for the specified {@link CommandPart}.
     */
    <V> List<V> getValues(CommandPart part);


    /**
     * Gets a {@link CommandPart} with a given name and uses it to get the converted values of the specified {@link CommandPart}
     *
     * @param partName The of the {@link CommandPart} to search values for.
     * @param <V>      The type of the returned value.
     * @return The {@link List} of values for the specified {@link CommandPart}.
     */
    default <V> List<V> getValues(String partName) {
        return getPart(partName).flatMap(this::getValues);
    }

    /**
     * Gets the first converted value of the specified {@link CommandPart}
     *
     * @param part The {@link CommandPart} to search values for.
     * @param <V>  The type of the returned value.
     * @return An {@link Optional} object containing the {@link List} of values for the specified {@link CommandPart}.
     */
    default <V> Optional<V> getValue(CommandPart part) {
        Optional<List<V>> values = getValues(part);

        return values.map(objects -> !objects.isEmpty() ? objects.get(0) : null);
    }

    /**
     * Gets a {@link CommandPart} with a given name and uses it to get the converted first value of the specified {@link CommandPart}
     *
     * @param partName The of the {@link CommandPart} to search values for.
     * @param <V>      The type of the returned value.
     * @return An {@link Optional} object containing the {@link List} of values for the specified {@link CommandPart}.
     */
    default <V> Optional<V> getValue(String partName) {
        return getPart(partName).flatMap(this::getValue);
    }

    /**
     * Gets if the specified {@link CommandPart} has a bound value(even if it is null)
     *
     * @param part The {@link CommandPart} to search for values.
     * @return An boolean value indicating whether the specified {@link Command} has a bound value.
     */
    boolean hasValue(CommandPart part);

    /**
     * Adds a value for the specified {@link CommandPart}
     *
     * @param part  The {@link CommandPart} to set values for.
     * @param value The value to add to the specified {@link CommandPart}
     * @param <T>   The type of the returned value.
     */
    <T> void setValue(CommandPart part, T value);

    /**
     * Adds a list of values for the specified {@link CommandPart}
     *
     * @param part   The {@link CommandPart} to set values for.
     * @param values The {@link List} of values with the generic type T to add into the specified {@link CommandPart}
     * @param <T>    The type of the returned value.
     */
    <T> void setValues(CommandPart part, List<T> values);

    /**
     * Sets a list of raw string values for the specified {@link CommandPart}
     *
     * @param part The {@link CommandPart} to set raw values for.
     * @param raw  The {@link List} of raw values to add into the specified {@link CommandPart}
     */
    void setRaw(CommandPart part, List<String> raw);
}
