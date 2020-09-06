package me.fixeddev.commandflow.part;

import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.stack.ArgumentStack;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BooleanPart extends PrimitivePart {

    public BooleanPart(String name, boolean consumeAll) {
        super(name, consumeAll);
    }

    public BooleanPart(String name) {
        super(name);
    }

    @Override
    public List<Boolean> parseValue(ArgumentStack stack) throws ArgumentParseException {
        List<Boolean> objects = new ArrayList<>();

        if (consumeAll) {
            while (stack.hasNext()) {
                objects.add(stack.nextBoolean());
            }
        } else {
            if (stack.hasNext()) {
                objects.add(stack.nextBoolean());
            }
        }

        return objects;
    }

    @Override
    public Type getType() {
        return boolean.class;
    }

}