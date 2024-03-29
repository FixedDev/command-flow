package team.unnamed.commandflow.part.defaults;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.part.ArgumentPart;
import team.unnamed.commandflow.stack.ArgumentStack;

import java.util.Collections;
import java.util.List;

/**
 * A part that has the option to use all the available primitive arguments
 * like {@linkplain Integer}, {@linkplain Double}, {@linkplain Float}, {@linkplain Boolean}, {@linkplain String}.
 */
public abstract class PrimitivePart implements ArgumentPart {

    private final String name;

    /**
     * Creates a PrimitivePart instance with the given name.
     *
     * @param name The name for this part.
     */
    public PrimitivePart(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        if (stack.hasNext()) {
            stack.next();
        }

        return Collections.emptyList();
    }

}