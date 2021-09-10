package me.fixeddev.commandflow.part;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.visitor.CommandPartVisitor;
import me.fixeddev.commandflow.stack.ArgumentStack;
import me.fixeddev.commandflow.stack.StackSnapshot;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface ArgumentPart extends CommandPart {
    @Override
    default @Nullable Component getLineRepresentation() {
        return Component.text("<" + getName() + ">");
    }

    default void parse(CommandContext context, ArgumentStack stack, CommandPart caller) throws ArgumentParseException {
        StackSnapshot snapshot = stack.getSnapshot();

        int oldArgumentsLeft = stack.getArgumentsLeft();
        List<? extends Object> value = parseValue(context, stack, caller);

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

    @Override
    default List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        if (stack.hasNext()) {
            stack.next();
        }

        return Collections.emptyList();
    }

    @Override
    default <T> T acceptVisitor(CommandPartVisitor<T> visitor) {
        return visitor.visit(this);
    }

    List<? extends Object> parseValue(CommandContext context, ArgumentStack stack, CommandPart caller) throws ArgumentParseException;

}
