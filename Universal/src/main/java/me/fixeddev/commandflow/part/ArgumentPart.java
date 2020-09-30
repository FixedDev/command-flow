package me.fixeddev.commandflow.part;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.stack.ArgumentStack;
import me.fixeddev.commandflow.stack.StackSnapshot;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public interface ArgumentPart extends CommandPart {
    @Override
    default @Nullable Component getLineRepresentation() {
        return TextComponent.builder("<" + getName() + ">").build();
    }

    default void parse(CommandContext context, ArgumentStack stack) throws ArgumentParseException {
        StackSnapshot snapshot = stack.getSnapshot();

        int oldArgumentsLeft = stack.getArgumentsLeft();
        List<? extends Object> value = parseValue(context, stack);

        List<String> rawArgs = new ArrayList<>();
        int usedArguments = oldArgumentsLeft - stack.getArgumentsLeft();

        if (usedArguments != 0) {
            stack.applySnapshot(snapshot);

            for (int i = 0; i < usedArguments; i++) {
                rawArgs.add(stack.next());
            }
        }

        context.setValues(this, value);
        context.setRaw(this, rawArgs);
    }

    List<? extends Object> parseValue(CommandContext context, ArgumentStack stack) throws ArgumentParseException;

    Type getType();
}
