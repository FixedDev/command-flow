package me.fixeddev.commandflow;

import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.part.CommandPart;

import java.util.*;

public class SimpleCommandContext implements CommandContext, Namespace {

    private final Namespace namespace;

    private Command executedCommand;
    private final List<Command> commandExecutionPath;
    private final List<String> rawArguments;
    private final List<String> labels;

    private final Set<CommandPart> allParts;
    private final Map<String, List<CommandPart>> allPartsByName;
    private final Map<CommandPart, List<String>> rawBindings;
    private final Map<CommandPart, List<Object>> valueBindings;

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
    public Optional<List<String>> getRaw(CommandPart part) {
        return Optional.ofNullable(rawBindings.get(part));
    }

    @Override
    public <V> Optional<List<V>> getValues(CommandPart part) {
        return Optional.ofNullable((List<V>) valueBindings.get(part));
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