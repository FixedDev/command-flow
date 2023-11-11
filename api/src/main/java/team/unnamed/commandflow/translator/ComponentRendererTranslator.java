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
package team.unnamed.commandflow.translator;

import team.unnamed.commandflow.Namespace;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.renderer.ComponentRenderer;
import net.kyori.adventure.text.renderer.TranslatableComponentRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.function.Function;

public class ComponentRendererTranslator implements Translator {

    private TranslationProvider provider;
    private ComponentRenderer<Namespace> renderer;

    public ComponentRendererTranslator(TranslationProvider provider, ComponentRenderer<Namespace> renderer) {
        this.provider = provider;
        this.renderer = renderer;
    }

    public ComponentRendererTranslator(TranslationProvider translationProvider) {
        provider = translationProvider;
        renderer = new TranslatableComponentRenderer<Namespace>() {
            @Override
            protected @Nullable MessageFormat translate(@NotNull String key, @NotNull Namespace context) {
                return new MessageFormat(provider.getTranslation(context, key));
            }
        };
    }

    @Override
    public Component translate(Component component, Namespace namespace) {
        return renderer.render(component, namespace);
    }

    @Override
    public void setProvider(TranslationProvider provider) {
        this.provider = provider;
    }

    @Override
    public void setConverterFunction(Function<String, Component> stringToComponent) {
        // NOP, this translator doesn't allow this
    }

}
