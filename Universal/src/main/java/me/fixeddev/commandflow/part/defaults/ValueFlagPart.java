package me.fixeddev.commandflow.part.defaults;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.ContextSnapshot;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.part.SinglePartWrapper;
import me.fixeddev.commandflow.stack.ArgumentStack;
import me.fixeddev.commandflow.stack.StackSnapshot;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ValueFlagPart implements SinglePartWrapper {

    private final CommandPart part;
    private final String name;
    private final String shortName;
    private final boolean allowFullName;

    public ValueFlagPart(String shortName, boolean allowFullName, CommandPart part) {
        this.name = part.getName();
        this.shortName = shortName;
        this.allowFullName = allowFullName;

        this.part = part;
    }

    public ValueFlagPart(String shortName, CommandPart part) {
        this.name = part.getName();
        this.shortName = shortName;
        this.allowFullName = false;

        this.part = part;
    }

    @Override
    public @Nullable Component getLineRepresentation() {
        TextComponent.Builder builder = TextComponent.builder("[")
                .append("-" + shortName + " ");

        if (part.getLineRepresentation() != null) {
            builder.append(part.getLineRepresentation());
        }

        builder.append("]");

        return builder.build();
    }

    @Override
    public CommandPart getPart() {
        return part;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack, CommandPart parent) throws ArgumentParseException {
        StackSnapshot snapshot = stack.getSnapshot();

        boolean found = false;

        while (stack.hasNext()) {
            String arg = stack.next();

            if (!arg.startsWith("-")) {
                continue;
            }

            if (arg.equals("--" + name) && allowFullName) {
                found = parseValueFlag(context, stack);

                break;
            }

            if (arg.equals("-" + shortName)) {
                found = parseValueFlag(context, stack);

                break;
            }
        }

        if (!found) {
            context.setValue(this, false);
        }

        stack.applySnapshot(snapshot, false);
    }

    @Override
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        String nextArgument = stack.hasNext() ? stack.next() : "";

        List<String> suggestions = new ArrayList<>();

        if (!nextArgument.startsWith("-")) {
            return suggestions;
        }

        nextArgument = nextArgument.lastIndexOf('-') != 0 ? nextArgument.substring(2) : nextArgument.substring(1);

        if (allowFullName&& name.startsWith(nextArgument)) {
            suggestions.add("--" + name);
        }

        if (shortName.startsWith(nextArgument)) {
            suggestions.add("-" + shortName);
        }

        if (nextArgument.equals(shortName) || nextArgument.equals(name)) {
            suggestions.clear();

            suggestions.addAll(part.getSuggestions(commandContext, stack));
        }

        return suggestions;
    }

    private boolean parseValueFlag(CommandContext context, ArgumentStack stack) {
        StackSnapshot beforeRemoveFlagStack = stack.getSnapshot();
        ContextSnapshot beforeParseContext = context.getSnapshot();

        stack.remove();
        int oldArgumentsLeft = stack.getArgumentsLeft();
        StackSnapshot beforeParseStack = stack.getSnapshot();

        // parse the next parts
        try {
            part.parse(context, stack, this);
        } catch (ArgumentParseException ex) {
            // ignore
            context.applySnapshot(beforeParseContext);
            stack.applySnapshot(beforeRemoveFlagStack);

            return false;
        }

        int usedArguments = oldArgumentsLeft - stack.getArgumentsLeft();

        if (usedArguments != 0) {
            stack.applySnapshot(beforeParseStack);
            // Otherwise it deletes the old cursor(element to remove - 1)
            stack.next();

            for (int i = 0; i < usedArguments; i++) {
                stack.remove();
            }

        }
        return true;
    }
}

