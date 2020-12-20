package me.fixeddev.commandflow.part.defaults;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.stack.ArgumentStack;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DoublePart extends PrimitivePart {

    public DoublePart(String name) {
        super(name);
    }

    @Override
    public List<Double> parseValue(CommandContext context, ArgumentStack stack) throws ArgumentParseException {
        return Collections.singletonList(stack.nextDouble());
    }

    @Override
    public Type getType() {
        return double.class;
    }

}