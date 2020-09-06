package me.fixeddev.commandflow.part.defaults;

import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.stack.ArgumentStack;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FloatPart extends PrimitivePart {

    public FloatPart(String name, boolean consumeAll) {
        super(name, consumeAll);
    }

    public FloatPart(String name) {
        super(name);
    }

    @Override
    public List<Float> parseValue(ArgumentStack stack) throws ArgumentParseException {
        List<Float> objects = new ArrayList<>();

        if (consumeAll) {
            while (stack.hasNext()) {
                objects.add(stack.nextFloat());
            }
        } else {
            if (stack.hasNext()) {
                objects.add(stack.nextFloat());
            }
        }

        return objects;
    }

    @Override
    public Type getType() {
        return float.class;
    }

}