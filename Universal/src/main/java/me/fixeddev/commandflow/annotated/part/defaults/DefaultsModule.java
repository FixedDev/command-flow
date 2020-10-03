package me.fixeddev.commandflow.annotated.part.defaults;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.annotated.annotation.ConsumedArgs;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.annotated.annotation.Text;
import me.fixeddev.commandflow.annotated.part.AbstractModule;
import me.fixeddev.commandflow.annotated.part.Key;
import me.fixeddev.commandflow.annotated.part.defaults.factory.ArgumentStackPartFactory;
import me.fixeddev.commandflow.annotated.part.defaults.factory.BooleanPartFactory;
import me.fixeddev.commandflow.annotated.part.defaults.factory.ContextFactory;
import me.fixeddev.commandflow.annotated.part.defaults.factory.DoublePartFactory;
import me.fixeddev.commandflow.annotated.part.defaults.factory.FloatPartFactory;
import me.fixeddev.commandflow.annotated.part.defaults.factory.IntegerPartFactory;
import me.fixeddev.commandflow.annotated.part.defaults.factory.StringPartFactory;
import me.fixeddev.commandflow.annotated.part.defaults.factory.StringTextPartFactory;
import me.fixeddev.commandflow.annotated.part.defaults.modifier.LimitModifier;
import me.fixeddev.commandflow.annotated.part.defaults.modifier.OptionalModifier;
import me.fixeddev.commandflow.stack.ArgumentStack;

public class DefaultsModule extends AbstractModule {
    @Override
    public void configure() {
        bindFactory(boolean.class, new BooleanPartFactory());
        bindFactory(double.class, new DoublePartFactory());
        bindFactory(float.class, new FloatPartFactory());
        bindFactory(int.class, new IntegerPartFactory());
        bindFactory(String.class, new StringPartFactory());
        bindFactory(new Key(String.class, Text.class), new StringTextPartFactory());
        bindFactory(CommandContext.class, new ContextFactory());
        bindFactory(ArgumentStack.class, new ArgumentStackPartFactory());

        bindModifier(ConsumedArgs.class, new LimitModifier());
        bindModifier(OptArg.class, new OptionalModifier());
    }
}
