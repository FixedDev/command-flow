package me.fixeddev.commandflow.translator;

import me.fixeddev.commandflow.Namespace;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.TextColor;

import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultTranslator implements Translator {
    private TranslationProvider provider;
    private Function<String, TextComponent> stringToComponent;

    public DefaultTranslator(TranslationProvider provider) {
        this(provider, Component::text);
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

        TextComponent.Builder componentBuilder = Component.text();
        convert(translatableComponent, componentBuilder, namespace);

        return componentBuilder.build();
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
    private final Pattern format = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");

    private void convert(TranslatableComponent component, TextComponent.Builder builder, Namespace namespace) {
        String trans = provider.getTranslation(namespace, component.key()); //translate

        if (trans == null || trans.isEmpty()) {
            builder.content(component.key());
            return;
        }
        TextColor lastColor = null;
        Component last = null;

        Matcher matcher = format.matcher(trans);
        int position = 0;
        int i = 0;
        while (matcher.find(position)) {
            int pos = matcher.start();
            if (pos != position) {
                builder.mergeStyle(component);
                last = stringToComponent.apply(trans.substring(position, pos));
                lastColor = lastColor(last);

                builder.append(last);
            }
            position = matcher.end();

            String formatCode = matcher.group(2);
            switch (formatCode.charAt(0)) {
                case 's':
                case 'd':
                    String withIndex = matcher.group(1);

                    List<Component> args = component.args();

                    int withIndexInt = withIndex != null ? Integer.parseInt(withIndex) - 1 : i++;

                    if (args.size() > withIndexInt) {
                        Component withComponent = component.args().get(withIndexInt);

                        if (last != null) {
                            withComponent = withComponent.style(withComponent.style()
                                    .colorIfAbsent(lastColor)
                                    .merge(last.style())
                            );
                        }

                        if (withComponent instanceof TranslatableComponent) {
                            convert(component, builder, namespace);
                        } else {
                            builder.append(withComponent);

                            last = withComponent;
                            lastColor = lastColor(last);
                        }

                    } else {
                        builder.append(stringToComponent.apply("%" + formatCode.charAt(0)));
                    }
                    break;
                case '%':
                    builder.append(stringToComponent.apply("%"));
                    break;
            }
        }
        if (trans.length() != position) {
            Component afterComponent = stringToComponent.apply(trans.substring(position));

            if (last != null) {
                afterComponent = afterComponent.style(afterComponent.style()
                        .colorIfAbsent(lastColor)
                        .merge(last.style())
                );
            }

            builder.append(afterComponent);
            last = afterComponent;
            lastColor = lastColor(last);
        }
    }

    private TextColor lastColor(Component component) {
        List<Component> extra = component.children();

        if (!extra.isEmpty()) {
            return lastColor(extra.get(extra.size() - 1));
        }

        return component.color();
    }
}
