package me.fixeddev.commandflow.translator;

import me.fixeddev.commandflow.Namespace;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

public class DefaultTranslator implements Translator {
    private TranslationProvider provider;
    private Function<String, TextComponent> stringToComponent;

    private static final Pattern FORMAT = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");

    public DefaultTranslator(TranslationProvider provider) {
        this(provider, Component::text);
    }

    public DefaultTranslator(TranslationProvider provider, Function<String, TextComponent> stringTextComponentFunction) {
        this.provider = provider;
        stringToComponent = stringTextComponentFunction;
    }

    @Override
    public Component translate(Component component, Namespace namespace) {
        if (component == null) {
            return Component.empty();
        }

        if (!(component instanceof TranslatableComponent)) {
            if (component.children().isEmpty()) {
                return component;
            }

            List<Component> children = new ArrayList<>(component.children().size());

            for (Component child : component.children()) {
                children.add(translate(child, namespace));
            }

            return component.children(children);
        }

        String key = ((TranslatableComponent) component).key();
        String trans = provider.getTranslation(namespace, key); //translate

        if (trans == null || trans.isEmpty()) {
            return Component.text(key);
        }

        return _translate((TranslatableComponent) component, namespace);
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

    private Component _translate(TranslatableComponent component, Namespace namespace) {
        String key = component.key();
        String trans = provider.getTranslation(namespace, key); //translate

        if (trans == null || trans.isEmpty()) {
            return Component.text(key);
        }

        Component newComponent = stringToComponent.apply(trans);

        // don't do this please.
        int[] iw = new int[]{0};
        newComponent = newComponent.replaceText(builder -> {
            builder.match(FORMAT)
                    .replacement((matcher, builder1) -> {
                        String formatCode = matcher.group(2);
                        List<Component> args = component.args();

                        switch (formatCode.charAt(0)) {
                            case 's':
                            case 'd':
                                String withIndex = matcher.group(1);

                                int withIndexInt = withIndex != null ? Integer.parseInt(withIndex) - 1 : iw[0]++;

                                if (args.size() > withIndexInt) {
                                    Component component1 = args.get(withIndexInt);

                                    return component1 instanceof TextComponent ? component1 : _translate(component, namespace);
                                } else {
                                    return Component.text("%" + formatCode.charAt(0));
                                }
                            case '%':
                                return Component.text("%");
                            default:
                                return builder1.asComponent();
                        }
                    });
        });


        return newComponent;
    }
}
