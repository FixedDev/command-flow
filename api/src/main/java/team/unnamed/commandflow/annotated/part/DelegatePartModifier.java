package team.unnamed.commandflow.annotated.part;

import team.unnamed.commandflow.part.CommandPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class DelegatePartModifier implements PartModifier {

    private final List<PartModifier> delegates;

    public DelegatePartModifier(List<PartModifier> delegates) {
        this.delegates = delegates;
    }

    @Override
    public CommandPart modify(CommandPart original, List<? extends Annotation> modifiers) {
        CommandPart part = original;
        for (PartModifier delegate : delegates) {
            part = delegate.modify(part, modifiers);
        }

        return part;
    }

}
