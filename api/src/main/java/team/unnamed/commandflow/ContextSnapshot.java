package team.unnamed.commandflow;

import team.unnamed.commandflow.command.Command;
import team.unnamed.commandflow.part.CommandPart;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * An immutable blackbox containing a copy of a {@link CommandContext} at a specific time,
 * being able to restore the state of a {@link CommandContext} to this state.
 *
 * @see CommandContext#applySnapshot(ContextSnapshot)
 */
public class ContextSnapshot {

    final Namespace namespace;

    final Command executedCommand;
    final List<Command> commandExecutionPath;
    final List<String> rawArguments;
    final List<String> labels;

    final Set<CommandPart> allParts;
    final Map<String, List<CommandPart>> allPartsByName;
    final Map<CommandPart, List<String>> rawBindings;
    final Map<CommandPart, List<Object>> valueBindings;

    public ContextSnapshot(Namespace namespace,
                           Command executedCommand,
                           List<Command> commandExecutionPath,
                           List<String> rawArguments,
                           List<String> labels,
                           Set<CommandPart> allParts,
                           Map<String, List<CommandPart>> allPartsByName,
                           Map<CommandPart, List<String>> rawBindings,
                           Map<CommandPart, List<Object>> valueBindings) {
        this.namespace = namespace;
        this.executedCommand = executedCommand;
        this.commandExecutionPath = commandExecutionPath;
        this.rawArguments = rawArguments;
        this.labels = labels;
        this.allParts = allParts;
        this.allPartsByName = allPartsByName;
        this.rawBindings = rawBindings;
        this.valueBindings = valueBindings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContextSnapshot)) return false;
        ContextSnapshot that = (ContextSnapshot) o;
        return Objects.equals(namespace, that.namespace) &&
                Objects.equals(executedCommand, that.executedCommand) &&
                Objects.equals(commandExecutionPath, that.commandExecutionPath) &&
                Objects.equals(rawArguments, that.rawArguments) &&
                Objects.equals(labels, that.labels) &&
                Objects.equals(allParts, that.allParts) &&
                Objects.equals(allPartsByName, that.allPartsByName) &&
                Objects.equals(rawBindings, that.rawBindings) &&
                Objects.equals(valueBindings, that.valueBindings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                namespace, executedCommand, commandExecutionPath, rawArguments, labels,
                allParts, allPartsByName, rawBindings, valueBindings
        );
    }

}
