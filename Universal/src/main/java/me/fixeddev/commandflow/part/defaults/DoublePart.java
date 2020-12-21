package me.fixeddev.commandflow.part.defaults;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.stack.ArgumentStack;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DoublePart extends PrimitivePart {

    private final double max;
    private final double min;

    private final boolean ranged;

    public DoublePart(String name) {
        this(name, 0, 0, false);
    }

    public DoublePart(String name, double min, double max) {
        this(name, min, max, true);
    }

    private DoublePart(String name, double min, double max, boolean ranged) {
        super(name);

        this.max = max;
        this.min = min;

        this.ranged = ranged;
    }

    @Override
    public List<Double> parseValue(CommandContext context, ArgumentStack stack) throws ArgumentParseException {
        double next = stack.nextFloat();
        if (ranged && (next > max || next < min)) {
            throw new ArgumentParseException();
        }

        return Collections.singletonList(next);
    }

    @Override
    public Type getType() {
        return double.class;
    }

}