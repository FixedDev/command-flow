package me.fixeddev.commandflow.part.defaults;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.stack.ArgumentStack;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BooleanPart extends PrimitivePart {

    public BooleanPart(String name, boolean consumeAll) {
        super(name, consumeAll);
    }

    public BooleanPart(String name) {
        super(name);
    }

    @Override
    public List<Boolean> parseValue(CommandContext context, ArgumentStack stack) throws ArgumentParseException {
        List<Boolean> objects = new ArrayList<>();

        if (consumeAll) {
            while (stack.hasNext()) {
                objects.add(stack.nextBoolean());
            }
        } else {
            objects.add(stack.nextBoolean());
        }

        return objects;
    }

    @Override
    public Type getType() {
        return boolean.class;
    }

    @Override
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        String prefix = stack.hasNext() ? stack.next() : "";

        if (consumeAll) {
            while (stack.hasNext()) {
                stack.next(); // ignored
            }

            prefix = stack.current();
        }

        if (prefix.isEmpty()) {
            return Arrays.asList("true", "false");
        }

        if(prefix.equalsIgnoreCase("true") || prefix.equalsIgnoreCase("false")){
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