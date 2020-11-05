package me.fixeddev.commandflow.part.defaults;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.stack.ArgumentStack;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A part that has the option to use all the available {@link String} arguments also with an option of
 * joining all the used String arguments into one with a specified separator between them.
 */
public class StringPart implements ArgumentPart {

    private String name;
    private boolean consumeAll;
    private boolean joinStrings;
    private String separator;

    /**
     * Creates a StringPart instance with the given name
     *
     * @param name        The name for this part.
     * @param consumeAll  If this part should consume all the available arguments.
     * @param joinStrings If the String values should be joined into one string instead of a {@link List} of Strings.
     * @param separator   The separator between the String values.
     */
    public StringPart(String name, boolean consumeAll, boolean joinStrings, String separator) {
        this.name = name;
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
    public List<String> parseValue(CommandContext context, ArgumentStack stack) throws ArgumentParseException {
        List<String> objects = new ArrayList<>();

        if (consumeAll) {
            while (stack.hasNext()) {
                objects.add(stack.next());
            }

            if (joinStrings) {
                return Collections.singletonList(String.join(separator, objects));
            }
        } else {
            objects.add(stack.next());
        }

        return objects;
    }

    @Override
    public Type getType() {
        return String.class;
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
