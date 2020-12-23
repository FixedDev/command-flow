package me.fixeddev.commandflow.part.defaults;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.stack.ArgumentStack;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.TranslatableComponent;

import java.lang.reflect.Type;
import java.util.*;
import java.util.List;

public class EnumPart extends PrimitivePart {

    private final Map<String, Object> enumConstants;
    private final String joinedEnumConstants;
    private final Class<?> enumClass;

    public EnumPart(String name, String joinDelimiter, Class<? extends Enum<?>> enumClass) {
        super(name);

        if (!enumClass.isEnum()) {
            throw new IllegalArgumentException("Given 'enumClass' isn't an enum type!");
        }

        this.enumClass = enumClass;
        this.enumConstants = new HashMap<>();

        for (Object enumConstant : enumClass.getEnumConstants()) {
            Enum<?> enumValue = (Enum<?>) enumConstant;

            enumConstants.put(enumValue.name().toLowerCase(), enumValue);
        }

        this.joinedEnumConstants = String.join(joinDelimiter, enumConstants.keySet());
    }

    public EnumPart(String name, Class<? extends Enum<?>> enumClass) {
        this(name, ", ", enumClass);
    }

    @Override
    public List<?> parseValue(CommandContext context, ArgumentStack stack) throws ArgumentParseException {

        String name = stack.next().toLowerCase();
        Object value = enumConstants.get(name);

        if (value == null) {
            Component message = TranslatableComponent.of("invalid.enum-value",
                    TextComponent.of(name),
                    TextComponent.of(joinedEnumConstants));

            throw new ArgumentParseException(message)
                    .setCommand(context.getCommand())
                    .setArgument(this);

        }

        return Collections.singletonList(value);
    }

    @Override
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        List<String> suggestions = new ArrayList<>();

        if (!stack.hasNext()) {
            suggestions.addAll(enumConstants.keySet());
            return suggestions;
        }

        String prefix = stack.next().toLowerCase();

        for (String name : enumConstants.keySet()) {
            if (name.startsWith(prefix)) {
                suggestions.add(name);
            }
        }

        return suggestions;
    }

    @Override
    public Type getType() {
        return enumClass;
    }

}
