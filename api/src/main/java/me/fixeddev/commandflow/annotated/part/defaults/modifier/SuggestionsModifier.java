package me.fixeddev.commandflow.annotated.part.defaults.modifier;

import me.fixeddev.commandflow.annotated.annotation.Suggestions;
import me.fixeddev.commandflow.annotated.part.PartModifier;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.part.defaults.SuggestionsModifierPart;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

public class SuggestionsModifier implements PartModifier {
    @Override
    public CommandPart modify(CommandPart original, List<? extends Annotation> modifiers) {
        Suggestions suggestions = getModifier(modifiers, Suggestions.class);

        return new SuggestionsModifierPart(original, Arrays.asList(suggestions.suggestions()));
    }
}
