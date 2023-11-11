/*
 * This file is part of commandflow, licensed under the MIT license
 *
 * Copyright (c) 2020-2023 Unnamed Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
