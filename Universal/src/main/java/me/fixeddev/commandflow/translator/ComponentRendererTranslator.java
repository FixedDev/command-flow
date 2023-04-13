package me.fixeddev.commandflow.translator;

import me.fixeddev.commandflow.Namespace;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
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
    public void setConverterFunction(Function<String, TextComponent> stringToComponent) {
        // NOP, this translator doesn't allow this
    }

}
