package me.fixeddev.commandflow.part;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.stack.ArgumentStack;
import net.kyori.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public interface CommandPart {
    String getName();

    @Nullable
    default Component getLineRepresentation() {
        return null;
    }

    default void parse(CommandContext context, ArgumentStack stack, @Nullable CommandPart caller) throws ArgumentParseException{
        parse(context, stack);
    }
    /**
     * @deprecated Should be replaced with {@link CommandPart#parse(CommandContext, ArgumentStack, CommandPart)}
     */
    @Deprecated
    void parse(CommandContext context, ArgumentStack stack) throws ArgumentParseException;


    default List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        return Collections.emptyList();
    }

    /**
     * If this part should be parsed on another thread
     *
     * @return A boolean indicating whether this part should be parsed on another thread.
     */
    default boolean isAsync() {
        return false;
    }
}
