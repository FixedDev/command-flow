package team.unnamed.commandflow.annotated.part.defaults.modifier;

import team.unnamed.commandflow.annotated.annotation.Suggestions;
import team.unnamed.commandflow.annotated.part.PartModifier;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.part.defaults.SuggestionsModifierPart;

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
