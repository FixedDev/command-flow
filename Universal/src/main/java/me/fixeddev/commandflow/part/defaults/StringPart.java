package me.fixeddev.commandflow.part.defaults;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A part that has the option to use all the available {@link String} arguments also with an option of
 * joining all the used String arguments into one with a specified separator between them.
 */
public class StringPart extends PrimitivePart {

    private final boolean consumeAll;
    private final boolean joinStrings;
    private final String separator;

    /**
     * Creates a StringPart instance with the given name
     *
     * @param name        The name for this part.
     * @param consumeAll  If this part should consume all the available arguments.
     * @param joinStrings If the String values should be joined into one string instead of a {@link List} of Strings.
     * @param separator   The separator between the String values.
     */
    public StringPart(String name, boolean consumeAll, boolean joinStrings, String separator) {
        super(name);
        this.consumeAll = consumeAll;
        this.joinStrings = joinStrings;
        this.separator = separator;
    }

    /**
     * Creates a StringPart instance with the given name and the default separator(a space).
     *
     * @param name        The name for this part.
     * @param consumeAll  If this part should consume all the available arguments.
     * @param joinStrings If the String values should be joined into one string instead of a {@link List} of Strings.
     */
    public StringPart(String name, boolean consumeAll, boolean joinStrings) {
        this(name, consumeAll, joinStrings, " ");
    }

    /**
     * Creates a StringPart instance with the given name and the joinStrings parameter as disabled.
     *
     * @param name       The name for this part.
     * @param consumeAll If this part should consume all the available arguments.
     */
    public StringPart(String name, boolean consumeAll) {
        this(name, consumeAll, false);
    }

    /**
     * Creates a StringPart instance with the given name and the joinStrings and consumeAll parameters disabled.
     *
     * @param name The name for this part.
     */
    public StringPart(String name) {
        this(name, false);
    }

    @Override
    public List<String> parseValue(CommandContext context, ArgumentStack stack, CommandPart parent) throws ArgumentParseException {
        List<String> objects = new ArrayList<>();

        String next = stack.next();
        objects.add(next);

        if (consumeAll) {
            while (stack.hasNext()) {
                objects.add(stack.next());
            }

            if (joinStrings) {
                return Collections.singletonList(String.join(separator, objects));
            }
        }

        return objects;
    }

    @Override
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        if (stack.hasNext()) {
            stack.next();
        }

        if (consumeAll) {
            while (stack.hasNext()) {
                stack.next(); // ignored, not needed
            }
        }

        return Collections.emptyList();
    }

    public boolean isConsumeAll() {
        return consumeAll;
    }
}
