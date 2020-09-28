package me.fixeddev.commandflow.translator;

import me.fixeddev.commandflow.Namespace;
import net.kyori.text.Component;

/**
 * An interface that allows to convert a {@link net.kyori.text.TranslatableComponent} into a
 * {@link net.kyori.text.TextComponent}.
 */
public interface Translator {
    /**
     * Translates the given {@link Component} if it is a {@link net.kyori.text.TranslatableComponent}
     * otherwise returns the same instance.
     *
     * @param namespace The {@link Namespace} of the command containing injected objects.
     * @param component The {@link Component} to translate
     * @return The translated {@link net.kyori.text.TextComponent} or the same instance if it isn't a {@link net.kyori.text.TranslatableComponent}
     */
    Component translate(Component component, Namespace namespace);
}
