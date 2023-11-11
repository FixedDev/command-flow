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
package team.unnamed.commandflow.annotated.part;

import team.unnamed.commandflow.annotated.modifier.CommandModifierFactory;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class SimplePartInjector implements PartInjector {

    private final Map<Key, PartFactory> factoryBindings;
    private final Map<Class<? extends Annotation>, PartModifier> modifiers;
    private final Map<Class<? extends Annotation>, CommandModifierFactory> commandModifiers;

    SimplePartInjector() {
        this.factoryBindings = new ConcurrentHashMap<>();
        this.modifiers = new ConcurrentHashMap<>();
        this.commandModifiers = new ConcurrentHashMap<>();
    }

    @Override
    public @Nullable PartFactory getFactory(Key key) {
        return factoryBindings.get(key);
    }

    @Override
    public @Nullable PartModifier getModifier(Class<? extends Annotation> annotation) {
        return modifiers.get(annotation);
    }

    @Override
    public void bindModifier(Class<? extends Annotation> annotation, PartModifier partModifier) {
        PartModifier old = modifiers.put(annotation, partModifier);

        if (old != null) {
            modifiers.put(annotation, old);

            throw new IllegalArgumentException("A modifier with the key " + annotation.toString() + " is already present!");
        }
    }

    @Override
    public void bindFactory(Key key, PartFactory factory) {
        PartFactory old = factoryBindings.put(key, factory);

        if (old != null) {
            factoryBindings.put(key, old);

            throw new IllegalArgumentException("A factory with the key " + key.toString() + " is already present!");
        }
    }

    @Override
    public CommandModifierFactory getCommandModifierFactory(Class<? extends Annotation> annotation) {
        return commandModifiers.get(annotation);
    }

    @Override
    public void bindCommandModifierFactory(Class<? extends Annotation> type, CommandModifierFactory factory) {
        commandModifiers.put(type, factory);
    }

    @Override
    public void install(Module module) {
        module.configure(this);
    }

}
