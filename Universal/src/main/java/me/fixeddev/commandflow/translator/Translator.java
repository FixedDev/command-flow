package me.fixeddev.commandflow.translator;

import me.fixeddev.commandflow.Namespace;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;

import java.util.function.Function;

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

    /**
     * Changes the {@link TranslationProvider} used for this Translator instance.
     *
     * @param provider The new {@link TranslationProvider} instance.
     */
    void setProvider(TranslationProvider provider);

    /**
     * Changes the Function used by this {@link Translator} to convert a String into a {@link TextComponent}.
     *
     * @param stringToComponent The new function used by this {@link Translator} instance.
     */
    void setConverterFunction(Function<String, TextComponent> stringToComponent);

}
