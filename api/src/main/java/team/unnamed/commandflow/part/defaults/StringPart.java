/*
 * This file is part of commandflow, licensed under the MIT license
 *
 * Copyright (c) 2020-2023 Unnamed Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package team.unnamed.commandflow.part.defaults;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.stack.ArgumentStack;

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
