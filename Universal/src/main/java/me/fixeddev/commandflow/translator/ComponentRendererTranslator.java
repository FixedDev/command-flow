package me.fixeddev.commandflow.translator;

import me.fixeddev.commandflow.Namespace;
import net.kyori.text.Component;
import net.kyori.text.renderer.ComponentRenderer;
import net.kyori.text.renderer.FriendlyComponentRenderer;

import java.text.MessageFormat;

public class ComponentRendererTranslator implements Translator {

    private TranslationProvider provider;
    private ComponentRenderer<Namespace> renderer;

    public ComponentRendererTranslator(TranslationProvider provider, ComponentRenderer<Namespace> renderer) {
        this.provider = provider;
        this.renderer = renderer;
    }

    public ComponentRendererTranslator(TranslationProvider translationProvider) {
        provider = translationProvider;
        renderer = FriendlyComponentRenderer.from((namespace, key) -> new MessageFormat(provider.getTranslation(namespace, key)));
    }

    @Override
    public Component translate(Component component, Namespace namespace) {
        return renderer.render(component, namespace);
    }

    @Override
    public void setProvider(TranslationProvider provider) {
        this.provider = provider;
    }
}
