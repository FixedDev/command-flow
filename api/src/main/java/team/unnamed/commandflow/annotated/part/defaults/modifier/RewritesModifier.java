package team.unnamed.commandflow.annotated.part.defaults.modifier;

import team.unnamed.commandflow.annotated.annotation.Rewrites;
import team.unnamed.commandflow.annotated.part.PartModifier;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.part.defaults.ArgumentRewriterPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class RewritesModifier implements PartModifier {

    @Override
    public CommandPart modify(CommandPart original, List<? extends Annotation> modifiers) {
        Rewrites rewrites = getModifier(modifiers, Rewrites.class);

        ArgumentRewriterPart rewriterPart = new ArgumentRewriterPart(original);

        for (Rewrites.Rewrite rewrite : rewrites.value()) {
            rewriterPart.addRewrite(rewrite.to(), rewrite.from());
        }

        return rewriterPart;
    }

}
