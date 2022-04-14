package me.fixeddev.commandflow;

import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.part.CommandPart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimpleCommandContext implements CommandContext, Namespace {

    private Namespace namespace;

    private Command executedCommand;
    private List<Command> commandExecutionPath;
    private List<String> rawArguments;
    private List<String> labels;

    private Set<CommandPart> allParts;
    private Map<String, List<CommandPart>> allPartsByName;
    private Map<CommandPart, List<String>> rawBindings;
    private Map<CommandPart, List<Object>> valueBindings;

    public SimpleCommandContext(Namespace namespace, List<String> rawArguments) {
        this.namespace = namespace;

        rawBindings = new HashMap<>();
        allParts = new HashSet<>();
        allPartsByName = new HashMap<>();
        valueBindings = new HashMap<>();

        commandExecutionPath = new ArrayList<>();
        this.rawArguments = rawArguments;
        labels = new ArrayList<>();
    }

    @Override
    public ContextSnapshot getSnapshot() {
        return new ContextSnapshot(namespace,
                executedCommand,
                new ArrayList<>(commandExecutionPath),
                new ArrayList<>(rawArguments),
                new ArrayList<>(labels),
                new HashSet<>(allParts),
                new HashMap<>(allPartsByName),
                new HashMap<>(rawBindings),
                new HashMap<>(valueBindings));
    }

    @Override
    public void applySnapshot(ContextSnapshot snapshot) {
        namespace = snapshot.namespace;
        executedCommand = snapshot.executedCommand;
        commandExecutionPath = snapshot.commandExecutionPath;
        rawArguments = snapshot.rawArguments;
        labels = snapshot.labels;
        allParts = snapshot.allParts;
        allPartsByName = snapshot.allPartsByName;
        rawBindings = snapshot.rawBindings;
        valueBindings = snapshot.valueBindings;
    }

    @Override
    public void setCommand(Command command, String label) {
        this.executedCommand = command;
        commandExecutionPath.add(command);
        labels.add(label);
    }

    @Override
    public void removeLastCommand() {
        commandExecutionPath.remove(executedCommand);
        executedCommand = commandExecutionPath.get(commandExecutionPath.size() - 1);
        labels.remove(labels.size() - 1);
    }

    @Override
    public Command getCommand() {
        return executedCommand;
    }

    @Override
    public List<Command> getExecutionPath() {
        return commandExecutionPath;
    }

    @Override
    public List<String> getArguments() {
        return rawArguments;
    }

    @Override
    public List<String> getLabels() {
        return labels;
    }

    @Override
    public List<CommandPart> getParts(String name) {
        return allPartsByName.computeIfAbsent(name, k -> new ArrayList<>());
    }

    @Override
    public boolean contains(CommandPart part) {
        return allParts.contains(part);
    }

    @Override
    public List<String> getRaw(CommandPart part) {
        return rawBindings.get(part);
    }

    @Override
    public <V> List<V> getValues(CommandPart part) {
        return (List<V>) valueBindings.get(part);
    }

    private void addPart(CommandPart part) {
        allParts.add(part);

        List<CommandPart> parts = allPartsByName
                .computeIfAbsent(part.getName(), key -> new ArrayList<>());

        if (!parts.contains(part)) {
            parts.add(part);
        }
    }

    @Override
    public boolean hasValue(CommandPart part) {
        return valueBindings.containsKey(part);
    }

    @Override
    public <T> void setValue(CommandPart part, T value) {
        addPart(part);

        valueBindings
                .computeIfAbsent(part, commandPart -> new ArrayList<>())
                .add(value);
    }

    @Override
    public <T> void setValues(CommandPart part, List<T> values) {
        addPart(part);

        valueBindings
                .computeIfAbsent(part, commandPart -> new ArrayList<>())
                .addAll(values);
    }

    @Override
    public void setRaw(CommandPart part, List<String> raw) {
        addPart(part);

        rawBindings.put(part, raw);
    }

    @Override
    public <T> T getObject(Class<T> clazz, String name) {
        return namespace.getObject(clazz, name);
    }

    @Override
    public <T> void setObject(Class<T> clazz, String name, T object) {
        namespace.setObject(clazz, name, object);
    }

}