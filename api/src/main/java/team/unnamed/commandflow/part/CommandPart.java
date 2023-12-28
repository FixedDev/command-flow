package team.unnamed.commandflow.part;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.part.visitor.CommandPartVisitor;
import team.unnamed.commandflow.stack.ArgumentStack;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface CommandPart {

    String getName();

    @Nullable
    default Component getLineRepresentation() {
        return null;
    }

    void parse(CommandContext context, ArgumentStack stack, @Nullable CommandPart caller) throws ArgumentParseException;

    @Nullable
    default List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        return null;
    }

    /**
     * If this part should be parsed on another thread
     *
     * @return A boolean indicating whether this part should be parsed on another thread.
     */
    default boolean isAsync() {
        return false;
    }

    default <T> T acceptVisitor(CommandPartVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
