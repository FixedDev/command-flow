package me.fixeddev.commandflow.part.defaults;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.stack.ArgumentStack;
import net.kyori.text.TextComponent;
import net.kyori.text.TranslatableComponent;

import java.lang.reflect.Type;
import java.util.*;

public class EnumPart extends PrimitivePart {

    private final Map<String, Object> enumConstants;
    private final String joinedEnumConstants;
    private final Class<?> enumClass;

    public EnumPart(String name, String joinDelimiter, Class<?> enumClass) {
        super(name);
        if (!enumClass.isEnum()) {
            throw new IllegalArgumentException("Given 'enumClass' isn't an enum type!");
        }

        this.enumClass = enumClass;
        this.enumConstants = new HashMap<>();
        for (Object enumConstant : enumClass.getEnumConstants()) {
            Enum<?> enumValue = (Enum<?>) enumConstant;
            enumConstants.put(enumValue.name(), enumValue);
        }
        // we can cache this because the enum constants never change (they are constants XD)
        this.joinedEnumConstants = String.join(joinDelimiter, enumConstants.keySet());
    }

    @Override
    public List<?> parseValue(CommandContext context, ArgumentStack stack) throws ArgumentParseException {

        String name = stack.next().toLowerCase();
        Object value = enumConstants.get(name);

        if (value == null) {
            throw new ArgumentParseException(
                    TranslatableComponent.of("invalid.enum-value",
                            TextComponent.of(name),
                            TextComponent.of(joinedEnumConstants))
            )
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
        } else {
            String prefix = stack.next().toLowerCase();
            for (String name : enumConstants.keySet()) {
                if (name.startsWith(prefix)) {
                    suggestions.add(name);
                }
            }
        }
        return suggestions;
    }

    @Override
    public Type getType() {
        return enumClass;
    }

}
