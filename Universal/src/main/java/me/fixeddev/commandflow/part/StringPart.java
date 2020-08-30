package me.fixeddev.commandflow.part;

import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.stack.ArgumentStack;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A part that uses all the available {@link String} arguments with the option of
 * joining all the used String arguments into one with a specified separator between them.
 */
public class StringPart implements ArgumentPart {

    private String name;
    private boolean joinStrings;
    private String separator;

    /**
     * Creates a StringPart instance with the given name
     *
     * @param name        The name for this part.
     * @param joinStrings If the String values should be joined into one string instead of a {@link List} of Strings.
     * @param separator   The separator between the String values.
     */
    public StringPart(String name, boolean joinStrings, String separator) {
        this.name = name;
        this.joinStrings = joinStrings;
        this.separator = separator;
    }

    /**
     * Creates a StringPart instance with the given name and the default separator(a space).
     *
     * @param name        The name for this part.
     * @param joinStrings If the String values should be joined into one string instead of a {@link List} of Strings.
     */
    public StringPart(String name, boolean joinStrings) {
        this(name, joinStrings, " ");
    }

    /**
     * Creates a StringPart instance with the given name and the joinStrings parameter as disabled.
     *
     * @param name The name for this part.
     */
    public StringPart(String name) {
        this(name, false);
    }

    @Override
    public List<String> parseValue(ArgumentStack stack) throws ArgumentParseException {
        List<String> objects = new ArrayList<>();

        while (stack.hasNext()) {
            objects.add(stack.next());
        }

        if (joinStrings) {
            return Collections.singletonList(String.join(separator, objects));
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
}
