package me.fixeddev.commandflow.part;

import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.stack.ArgumentStack;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DoublePart extends PrimitivePart {

    public DoublePart(String name, boolean consumeAll) {
        super(name, consumeAll);
    }

    public DoublePart(String name) {
        super(name);
    }

    @Override
    public List<Double> parseValue(ArgumentStack stack) throws ArgumentParseException {
        List<Double> objects = new ArrayList<>();

        if (consumeAll) {
            while (stack.hasNext()) {
                objects.add(stack.nextDouble());
            }
        } else {
            if (stack.hasNext()) {
                objects.add(stack.nextDouble());
            }
        }

        return objects;
    }

    @Override
    public Type getType() {
        return double.class;
    }

}