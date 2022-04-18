package me.fixeddev.commandflow.translator;

import me.fixeddev.commandflow.ComponentUtil;
import me.fixeddev.commandflow.Namespace;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.TranslatableComponent;

import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

public class DefaultTranslator implements Translator {

    private TranslationProvider provider;
    private Function<String, TextComponent> stringToComponent;

    private static final Pattern FORMAT = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");

    public DefaultTranslator(TranslationProvider provider) {
        this(provider, TextComponent::of);
    }

    public DefaultTranslator(TranslationProvider provider, Function<String, TextComponent> stringTextComponentFunction) {
        this.provider = provider;
        stringToComponent = stringTextComponentFunction;
    }

    @Override
    public Component translate(Component component, Namespace namespace) {
        if (!(component instanceof TranslatableComponent)) {
            return component;
        }

        TranslatableComponent translatableComponent = (TranslatableComponent) component;

        return newConvert(translatableComponent, namespace);
    }

    @Override
    public void setProvider(TranslationProvider provider) {
        if (provider == null) {
            throw new IllegalArgumentException("The provided provider is null!");
        }

        this.provider = provider;
    }


    @Override
    public void setConverterFunction(Function<String, TextComponent> stringToComponent) {
        this.stringToComponent = stringToComponent;
    }


    // Taken from BungeeCord-Chat TranslatableComponent and modified to allow the conversion of a TranslatableComponent into a Text Component
    // instead of it being converted into plain text
    private TextComponent newConvert(TranslatableComponent component, Namespace namespace) {
        String trans = provider.getTranslation(namespace, component.key()); //translate

        if (trans == null || trans.isEmpty()) {
            return TextComponent.of(component.key());
        }

        TextComponent translated = TextComponent.builder(trans).style(component.style()).build();

        // don't do this please.
        int[] iw = new int[]{0};
        return (TextComponent) ComponentUtil.recursiveDynamicReplace(translated, FORMAT, (s, matcher) -> {
            String formatCode = matcher.group(2);
            switch (formatCode.charAt(0)) {
                case 's':
                case 'd':
                    String withIndex = matcher.group(1);

                    List<Component> args = component.args();

                    int withIndexInt = withIndex != null ? Integer.parseInt(withIndex) - 1 : iw[0]++;

                    if (args.size() > withIndexInt) {
                        Component component1 = component.args().get(withIndexInt);

                        return component1 instanceof TextComponent ? (TextComponent) component1 : newConvert(component, namespace);
                    } else {
                        return stringToComponent.apply("%" + formatCode.charAt(0));
                    }
                case '%':
                    return stringToComponent.apply("%");
                default:
                    return TextComponent.of(s);
            }
        });
    }

}
