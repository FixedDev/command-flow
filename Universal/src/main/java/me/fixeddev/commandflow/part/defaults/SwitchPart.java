package me.fixeddev.commandflow.part.defaults;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import me.fixeddev.commandflow.stack.StackSnapshot;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SwitchPart implements CommandPart {

    private final String name;
    private final String shortName;
    private final boolean allowFullName;

    public SwitchPart(String name, String shortName, boolean allowFullName) {
        this.name = name;
        this.shortName = shortName;
        this.allowFullName = allowFullName;
    }

    public SwitchPart(String shortName) {
        this.name = shortName;
        this.shortName = shortName;
        this.allowFullName = false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public @Nullable Component getLineRepresentation() {
        TextComponent.Builder builder = TextComponent.builder("[");
        builder.append("-" + shortName);
        builder.append("]");

        return builder.build();
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
                stack.remove();

                context.setValue(this, true);
                found = true;

                break;
            }

            if (arg.equals("-" + shortName)) {
                stack.remove();

                context.setValue(this, true);
                found = true;

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

        if (allowFullName && name.startsWith(nextArgument)) {
            suggestions.add("--" + name);
        }

        if (shortName.startsWith(nextArgument)) {
            suggestions.add("-" + shortName);
        }

        if(nextArgument.equals(shortName) || nextArgument.equals(name)){
            suggestions.clear();

            return suggestions;
        }

        return suggestions;
    }

}
