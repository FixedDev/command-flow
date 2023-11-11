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

import team.unnamed.commandflow.part.CommandPart;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * A default implementation of {@linkplain Module}, you just need to implement the configure method.
 */
public abstract class AbstractModule implements Module {

    private PartInjector injector;

    @Override
    public final void configure(PartInjector injector) {
        this.injector = injector;
        configure();
        this.injector = null;
    }

    /**
     * Returns the current {@linkplain PartInjector} used for bindings
     *
     * @return The {@linkplain PartInjector} that every bind method is being delegated to.
     */
    protected final PartInjector getInjector() {
        if (this.injector == null) {
            throw new IllegalStateException("The bind methods only can be called when the module is installed on an injector!");
        }
        return this.injector;
    }

    protected final void bindModifier(Class<? extends Annotation> annotation, PartModifier partModifier) {
        getInjector().bindModifier(annotation, partModifier);
    }

    protected final void bindFactory(Type type, PartFactory partFactory) {
        getInjector().bindFactory(type, partFactory);
    }

    protected final void bindFactory(Key key, PartFactory factory) {
        getInjector().bindFactory(key, factory);
    }

    protected final void bind(Key key, CommandPart part) {
        getInjector().bindPart(key, part);
    }

    protected final void bind(Type type, CommandPart part) {
        getInjector().bindPart(type, part);
    }

    public abstract void configure();

}
