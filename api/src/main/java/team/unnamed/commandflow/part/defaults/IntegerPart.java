package team.unnamed.commandflow.part.defaults;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.stack.ArgumentStack;
import net.kyori.adventure.text.Component;

import java.util.Collections;
import java.util.List;

import static net.kyori.adventure.text.Component.text;

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
            Component message = Component.translatable("number.out-range", text(next), text(min), text(max));

            throw new ArgumentParseException(message);
        }

        return Collections.singletonList(next);
    }

}