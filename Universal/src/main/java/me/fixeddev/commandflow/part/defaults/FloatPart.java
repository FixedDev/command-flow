package me.fixeddev.commandflow.part.defaults;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.stack.ArgumentStack;
import net.kyori.text.Component;
import net.kyori.text.TranslatableComponent;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import static net.kyori.text.TextComponent.of;

public class FloatPart extends PrimitivePart {

    private final float max;
    private final float min;

    private final boolean ranged;

    public FloatPart(String name) {
        this(name, 0, 0, false);
    }

    public FloatPart(String name, float min, float max) {
        this(name, min, max, true);
    }

    private FloatPart(String name, float min, float max, boolean ranged) {
        super(name);

        this.max = max;
        this.min = min;

        this.ranged = ranged;
    }

    @Override
    public List<Float> parseValue(CommandContext context, ArgumentStack stack) throws ArgumentParseException {
        float next = stack.nextFloat();
        if (ranged && (next > max || next < min)) {
            Component message = TranslatableComponent.of("number.out-range", of(next), of(min), of(max));

            throw new ArgumentParseException(message);
        }

        return Collections.singletonList(next);
    }

    @Override
    public Type getType() {
        return float.class;
    }

}