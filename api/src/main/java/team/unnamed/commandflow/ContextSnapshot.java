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
