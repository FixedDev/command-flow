package team.unnamed.commandflow.part.defaults;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.stack.ArgumentStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A {@linkplain CommandPart} that parses one argument as a boolean, with the values true or false being allowed.
 */
public class BooleanPart extends PrimitivePart {

    public BooleanPart(String name) {
        super(name);
    }

    @Override
    public List<Boolean> parseValue(CommandContext context, ArgumentStack stack, CommandPart parent) throws ArgumentParseException {
        return Collections.singletonList(stack.nextBoolean());
    }

    @Override
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        String prefix = stack.hasNext() ? stack.next() : null;

        if (prefix == null) {
            return Collections.emptyList();
        }

        if (prefix.isEmpty()) {
            return Arrays.asList("true", "false");
        }

        if (prefix.equalsIgnoreCase("true") || prefix.equalsIgnoreCase("false")) {
            return Collections.emptyList();
        }

        if (prefix.startsWith("t")) {
            return Collections.singletonList("true");
        }

        if (prefix.startsWith("f")) {
            return Collections.singletonList("false");
        }

        return Collections.emptyList();
    }

}