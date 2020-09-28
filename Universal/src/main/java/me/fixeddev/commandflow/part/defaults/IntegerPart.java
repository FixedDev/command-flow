package me.fixeddev.commandflow.part.defaults;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.stack.ArgumentStack;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class IntegerPart extends PrimitivePart {

    public IntegerPart(String name, boolean consumeAll) {
        super(name, consumeAll);
    }

    public IntegerPart(String name) {
        super(name);
    }

    @Override
    public List<Integer> parseValue(CommandContext context, ArgumentStack stack) throws ArgumentParseException {
        List<Integer> objects = new ArrayList<>();

        if (consumeAll) {
            while (stack.hasNext()) {
                objects.add(stack.nextInt());
            }
        } else {
            objects.add(stack.nextInt());
        }

        return objects;
    }

    @Override
    public Type getType() {
        return int.class;
    }

}