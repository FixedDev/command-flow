package me.fixeddev.commandflow.part.defaults;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.stack.ArgumentStack;

import java.util.Collections;
import java.util.List;

/**
 * A part that has the option to use all the available primitive arguments
 * like {@linkplain Integer}, {@linkplain Double}, {@linkplain Float}, {@linkplain Boolean}.
 */
public abstract class PrimitivePart implements ArgumentPart {

    private String name;
    protected boolean consumeAll;

    /**
     * Creates a PrimitivePart instance with the given name and boolean that represents if
     * the arguments will be consumed.
     *
     * @param name       The name for this part.
     * @param consumeAll If this part should consume all the available arguments.
     */
    public PrimitivePart(String name, boolean consumeAll) {
        this.name = name;
        this.consumeAll = consumeAll;
    }

    /**
     * Creates a PrimitivePart instance with the given name and consumeAll parameter as disabled.
     *
     * @param name The name for this part.
     */
    public PrimitivePart(String name) {
        this(name, false);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        stack.next();

        if (consumeAll) {
            while (stack.hasNext()) {
                stack.next(); // ignored, not needed
            }
        }

        return Collections.emptyList();
    }

}