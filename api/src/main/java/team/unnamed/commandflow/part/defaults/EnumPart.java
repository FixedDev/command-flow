package team.unnamed.commandflow.part.defaults;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.stack.ArgumentStack;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A {@linkplain CommandPart} that allows the parsing of every enum type.
 * <p>
 * This implementation eagerly loads the enum values from the specified class when created, allowing for fast lookup of
 * enum values when parsing.
 */
public class EnumPart extends PrimitivePart {

    private final Map<String, Object> enumConstants;
    private final String joinedEnumConstants;
    private final Class<?> enumClass;

    /**
     * Creates an EnumPart with a specified name and enum type. This also supports setting the join delimiter, that's used to
     * separate the different enum values on invalid value message.
     *
     * @param name          The name of this EnumPart.
     * @param joinDelimiter The delimiter between different enum values.
     * @param enumClass     The enum type for this EnumPart.
     */
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

    /**
     * Creates an EnumPart with a specified name and enum type and with the default delimiter ", ".
     *
     * @param name          The name of this EnumPart.
     * @param enumClass     The enum type for this EnumPart.
     */
    public EnumPart(String name, Class<? extends Enum<?>> enumClass) {
        this(name, ", ", enumClass);
    }

    @Override
    public List<?> parseValue(CommandContext context, ArgumentStack stack, CommandPart parent) throws ArgumentParseException {

        String name = stack.next().toLowerCase();
        Object value = enumConstants.get(name);

        if (value == null) {
            Component message = Component.translatable("invalid.enum-value")
                    .args(
                            Component.text(name),
                            Component.text(joinedEnumConstants)
                    );

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
            return Collections.emptyList();
        }

        String prefix = stack.next().toLowerCase();

        for (String name : enumConstants.keySet()) {
            if (name.startsWith(prefix)) {
                suggestions.add(name);
            }
        }

        return suggestions;
    }

}
