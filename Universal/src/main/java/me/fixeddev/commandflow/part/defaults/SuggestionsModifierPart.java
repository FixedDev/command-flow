package me.fixeddev.commandflow.part.defaults;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SuggestionsModifierPart implements CommandPart {

    private final CommandPart part;
    private final List<String> suggestions;

    public SuggestionsModifierPart(CommandPart part, List<String> suggestions) {
        this.part = part;
        this.suggestions = suggestions;
    }

    @Override
    public String getName() {
        return part.getName() + "-suggestions";
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack, @Nullable CommandPart caller) throws ArgumentParseException {
        part.parse(context, stack, caller);
    }

    @Override
    public @Nullable List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        if (!stack.hasNext()) {
            return Collections.emptyList();
        }

        List<String> suggestions = new ArrayList<>();
        String prefix = stack.next().toLowerCase();

        for (String suggestion : suggestions) {
            if (suggestion.toLowerCase().startsWith(prefix)) {
                suggestions.add(suggestion);
            }
        }

        return suggestions;
    }
}
