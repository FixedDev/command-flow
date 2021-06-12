package me.fixeddev.commandflow.part.defaults;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import net.kyori.text.Component;
import net.kyori.text.TranslatableComponent;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.kyori.text.TextComponent.of;

/**
 * A {@linkplain me.fixeddev.commandflow.part.CommandPart} that parses one argument as a double.
 * <p>
 * This {@linkplain me.fixeddev.commandflow.part.CommandPart} also supports having only a permitted range.
 */
public class DoublePart extends PrimitivePart {

    private final double max;
    private final double min;

    private final boolean ranged;

    /**
     * Creates a DoublePart without a range, with the specified name.
     *
     * @param name The name of this DoublePart.
     */
    public DoublePart(String name) {
        this(name, 0, 0, false);
    }

    /**
     * Creates a DoublePart with a minimum and maximum value range and a specified name.
     *
     * @param name The name of this DoublePart.
     * @param max  The maximum value(exclusive) allowed for this part.
     * @param min  The minimum value(exclusive) allowed for this part.
     */
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
    public List<Double> parseValue(CommandContext context, ArgumentStack stack, CommandPart parent) throws ArgumentParseException {
        double next = stack.nextDouble();
        if (ranged && (next > max || next < min)) {
            Component message = TranslatableComponent.of("number.out-range", of(next), of(min), of(max));

            throw new ArgumentParseException(message);
        }

        return Collections.singletonList(next);
    }

    @Override
    public Type getType() {
        return double.class;
    }

}