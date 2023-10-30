package team.unnamed.commandflow.annotated.part.defaults;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.annotated.annotation.Flag;
import team.unnamed.commandflow.annotated.annotation.Limit;
import team.unnamed.commandflow.annotated.annotation.OptArg;
import team.unnamed.commandflow.annotated.annotation.Rewrites;
import team.unnamed.commandflow.annotated.annotation.Suggestions;
import team.unnamed.commandflow.annotated.annotation.Switch;
import team.unnamed.commandflow.annotated.annotation.Text;
import team.unnamed.commandflow.annotated.part.AbstractModule;
import team.unnamed.commandflow.annotated.part.Key;
import me.fixeddev.commandflow.annotated.part.defaults.factory.*;
import team.unnamed.commandflow.annotated.part.defaults.modifier.LimitModifier;
import team.unnamed.commandflow.annotated.part.defaults.modifier.OptionalModifier;
import team.unnamed.commandflow.annotated.part.defaults.modifier.RewritesModifier;
import team.unnamed.commandflow.annotated.part.defaults.modifier.SuggestionsModifier;
import team.unnamed.commandflow.annotated.part.defaults.modifier.ValueFlagModifier;
import team.unnamed.commandflow.stack.ArgumentStack;
import team.unnamed.commandflow.annotated.part.defaults.factory.*;

public class DefaultsModule extends AbstractModule {

    @Override
    public void configure() {
        BooleanPartFactory booleanPartFactory = new BooleanPartFactory();
        bindFactory(boolean.class, booleanPartFactory);
        bindFactory(Boolean.class, booleanPartFactory);

        DoublePartFactory doublePartFactory = new DoublePartFactory();
        bindFactory(double.class, doublePartFactory);
        bindFactory(Double.class, doublePartFactory);

        FloatPartFactory floatPartFactory = new FloatPartFactory();
        bindFactory(Float.class, floatPartFactory);
        bindFactory(float.class, floatPartFactory);

        IntegerPartFactory partFactory = new IntegerPartFactory();
        bindFactory(int.class, partFactory);
        bindFactory(Integer.class, partFactory);

        LongPartFactory longPartFactory = new LongPartFactory();
        bindFactory(long.class, longPartFactory);
        bindFactory(Long.class, longPartFactory);

        bindFactory(String.class, new StringPartFactory());

        bindFactory(new Key(String.class, Text.class), new StringTextPartFactory());
        bindFactory(CommandContext.class, new ContextFactory());
        bindFactory(ArgumentStack.class, new ArgumentStackPartFactory());

        SwitchPartFactory flagPartFactory = new SwitchPartFactory();

        bindFactory(new Key(boolean.class, Switch.class), flagPartFactory);
        bindFactory(new Key(Boolean.class, Switch.class), flagPartFactory);

        bindModifier(Limit.class, new LimitModifier());
        bindModifier(OptArg.class, new OptionalModifier());
        bindModifier(Flag.class, new ValueFlagModifier());
        bindModifier(Rewrites.class, new RewritesModifier());
        bindModifier(Suggestions.class, new SuggestionsModifier());
    }

}
