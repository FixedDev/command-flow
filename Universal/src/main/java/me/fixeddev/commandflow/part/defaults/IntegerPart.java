package me.fixeddev.commandflow.part.defaults;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import net.kyori.adventure.text.Component;

import java.util.Collections;
import java.util.List;

public class IntegerPart extends PrimitivePart {

    private final int max;
    private final int min;

    private final boolean ranged;

    public IntegerPart(String name) {
        this(name, 0, 0, false);
    }

    public IntegerPart(String name, int min, int max) {
        this(name, min, max, true);
    }

    private IntegerPart(String name, int min, int max, boolean ranged) {
        super(name);

        this.max = max;
        this.min = min;

        this.ranged = ranged;
    }

    @Override
    public List<Integer> parseValue(CommandContext context, ArgumentStack stack, CommandPart parent) throws ArgumentParseException {
        int next = stack.nextInt();
        if (ranged && (next > max || next < min)) {
            Component message = Component.translatable("number.out-range").args(Component.text(next), Component.text(min), Component.text(max));

            throw new ArgumentParseException(message);
        }

        return Collections.singletonList(next);
    }

}