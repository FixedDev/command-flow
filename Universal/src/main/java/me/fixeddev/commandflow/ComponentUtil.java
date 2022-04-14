package me.fixeddev.commandflow;

import net.kyori.text.Component;
import net.kyori.text.TextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComponentUtil {

    public static Component basicReplace(Component component, Pattern pattern, TextComponent replacement) {
        if (!(component instanceof TextComponent)) {
            return component;
        }

        TextComponent textComponent = (TextComponent) component;

        TextComponent.Builder parentComponent = TextComponent.builder();
        String content = textComponent.content();

        Matcher matcher = pattern.matcher(content);

        int last = 0;

        TextComponent lastComponent = textComponent;

        while (matcher.find()) {
            String textBefore = content.substring(last, matcher.start());

            parentComponent.append(lastComponent.toBuilder()
                    .content(textBefore).build());

            last = matcher.end() + 1;
            parentComponent.append(lastComponent = replacement);
        }

        if (last < content.length()) {
            String textAfter = content.substring(last);

            Component componentToAppend = lastComponent.toBuilder()
                    .content(textAfter)
                    .build()
                    .children(new ArrayList<>());

            parentComponent.append(componentToAppend);

        }

        return parentComponent.build();
    }

    public static Component recursiveBasicReplace(Component component, Pattern pattern, TextComponent replacement) {
        List<Component> children = new ArrayList<>(component.children());

        component = basicReplace(component, pattern, replacement);

        children.replaceAll(component1 -> recursiveBasicReplace(component1, pattern, replacement));
        component.children(children);

        return component;
    }

    public static Component dynamicReplace(Component component, Pattern pattern, BiFunction<String, Matcher, TextComponent> replacementProvider) {
        if (!(component instanceof TextComponent)) {
            return component;
        }

        TextComponent textComponent = (TextComponent) component;

        TextComponent.Builder parentComponent = TextComponent.builder();
        String content = textComponent.content();
        char[] array = content.toCharArray();

        Matcher matcher = pattern.matcher(content);

        int lastIdx = 0;

        TextComponent lastComponent = textComponent;

        boolean matchedOnce = false;

        while (matcher.find()) {
            matchedOnce = true;
            int start = matcher.start();
            int end = matcher.end();

            String textBefore = new String(Arrays.copyOfRange(array, lastIdx, start));
            String toReplace = new String(Arrays.copyOfRange(array, start, end));

            parentComponent.append(lastComponent.toBuilder()
                    .content(textBefore).build());

            lastIdx = end;

            TextComponent replacement = replacementProvider.apply(toReplace, matcher);

            lastComponent = (TextComponent) recursiveDynamicReplace(replacement, pattern, replacementProvider);
            parentComponent.append(lastComponent);
        }

        if (!matchedOnce) {
            return component;
        }

        if (lastIdx < content.length()) {
            String textAfter = content.substring(lastIdx);

            Component componentToAppend = lastComponent.toBuilder()
                    .content(textAfter)
                    .build()
                    .children(new ArrayList<>());

            parentComponent.append(componentToAppend);

        }

        return parentComponent.build();
    }

    public static Component recursiveDynamicReplace(Component component, Pattern pattern, BiFunction<String, Matcher, TextComponent> replacementProvider) {
        List<Component> children = new ArrayList<>(component.children());

        component = dynamicReplace(component, pattern, replacementProvider);

        children.replaceAll(component1 -> recursiveDynamicReplace(component1, pattern, replacementProvider));
        component.children(children);

        return component;
    }

}
