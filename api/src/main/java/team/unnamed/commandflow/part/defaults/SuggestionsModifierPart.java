package team.unnamed.commandflow.part.defaults;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.stack.ArgumentStack;
import net.kyori.adventure.text.Component;
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
    public @Nullable Component getLineRepresentation() {
        return part.getLineRepresentation();
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

        for (String suggestion : this.suggestions) {
            if (suggestion.toLowerCase().startsWith(prefix)) {
                suggestions.add(suggestion);
            }
        }

        return suggestions;
    }

    @Override
    public boolean isAsync() {
        return part.isAsync();
    }

    public CommandPart getPart() {
        return part;
    }
}
