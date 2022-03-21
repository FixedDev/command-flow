package me.fixeddev.commandflow.annotated.part.defaults;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.annotated.annotation.Limit;
import me.fixeddev.commandflow.annotated.annotation.Flag;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.annotated.annotation.Rewrites;
import me.fixeddev.commandflow.annotated.annotation.Switch;
import me.fixeddev.commandflow.annotated.annotation.Text;
import me.fixeddev.commandflow.annotated.part.AbstractModule;
import me.fixeddev.commandflow.annotated.part.Key;
import me.fixeddev.commandflow.annotated.part.defaults.factory.ArgumentStackPartFactory;
import me.fixeddev.commandflow.annotated.part.defaults.factory.BooleanPartFactory;
import me.fixeddev.commandflow.annotated.part.defaults.factory.ContextFactory;
import me.fixeddev.commandflow.annotated.part.defaults.factory.DoublePartFactory;
import me.fixeddev.commandflow.annotated.part.defaults.factory.LongPartFactory;
import me.fixeddev.commandflow.annotated.part.defaults.factory.SwitchPartFactory;
import me.fixeddev.commandflow.annotated.part.defaults.factory.FloatPartFactory;
import me.fixeddev.commandflow.annotated.part.defaults.factory.IntegerPartFactory;
import me.fixeddev.commandflow.annotated.part.defaults.factory.StringPartFactory;
import me.fixeddev.commandflow.annotated.part.defaults.factory.StringTextPartFactory;
import me.fixeddev.commandflow.annotated.part.defaults.modifier.LimitModifier;
import me.fixeddev.commandflow.annotated.part.defaults.modifier.OptionalModifier;
import me.fixeddev.commandflow.annotated.part.defaults.modifier.RewritesModifier;
import me.fixeddev.commandflow.annotated.part.defaults.modifier.ValueFlagModifier;
import me.fixeddev.commandflow.stack.ArgumentStack;

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
    }
}
