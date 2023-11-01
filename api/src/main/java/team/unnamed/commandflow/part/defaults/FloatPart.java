package team.unnamed.commandflow.part.defaults;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.stack.ArgumentStack;
import net.kyori.adventure.text.Component;

import java.util.Collections;
import java.util.List;

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
    public List<Float> parseValue(CommandContext context, ArgumentStack stack, CommandPart parent) throws ArgumentParseException {
        float next = stack.nextFloat();
        if (ranged && (next > max || next < min)) {
            Component message = Component.translatable("number.out-range").args(Component.text(next), Component.text(min), Component.text(max));

            throw new ArgumentParseException(message);
        }

        return Collections.singletonList(next);
    }

}