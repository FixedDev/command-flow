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
import team.unnamed.commandflow.ContextSnapshot;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.part.SinglePartWrapper;
import team.unnamed.commandflow.stack.ArgumentStack;
import team.unnamed.commandflow.stack.StackSnapshot;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.Nullable;

public class ValueFlagPart implements SinglePartWrapper {

    private final CommandPart part;
    private final String name;
    private final String shortName;
    private final boolean allowFullName;

    public ValueFlagPart(String shortName, boolean allowFullName, CommandPart part) {
        this.name = part.getName();
        this.shortName = shortName;
        this.allowFullName = allowFullName;

        this.part = part;
    }

    public ValueFlagPart(String shortName, CommandPart part) {
        this.name = part.getName();
        this.shortName = shortName;
        this.allowFullName = false;

        this.part = part;
    }

    @Override
    public @Nullable Component getLineRepresentation() {
        TextComponent.Builder builder = Component.text()
                .append(Component.text("["))
                .append(Component.text("-" + shortName + " "));

        if (part.getLineRepresentation() != null) {
            builder.append(part.getLineRepresentation());
        }

        builder.append(Component.text("]"));

        return builder.build();
    }

    @Override
    public CommandPart getPart() {
        return part;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack, CommandPart parent) throws ArgumentParseException {
        StackSnapshot snapshot = stack.getSnapshot();

        boolean found = false;

        while (stack.hasNext()) {
            String arg = stack.next();

            if (!arg.startsWith("-")) {
                continue;
            }

            if (arg.equals("--" + name) && allowFullName) {
                found = parseValueFlag(context, stack);

                break;
            }

            if (arg.equals("-" + shortName)) {
                found = parseValueFlag(context, stack);

                break;
            }
        }

        if (!found) {
            context.setValue(this, false);
        }

        stack.applySnapshot(snapshot, false);
    }

    private boolean parseValueFlag(CommandContext context, ArgumentStack stack) {
        StackSnapshot beforeRemoveFlagStack = stack.getSnapshot();
        ContextSnapshot beforeParseContext = context.getSnapshot();

        stack.remove();
        int oldArgumentsLeft = stack.getArgumentsLeft();
        StackSnapshot beforeParseStack = stack.getSnapshot();

        // parse the next parts
        try {
            part.parse(context, stack, this);
        } catch (ArgumentParseException ex) {
            // ignore
            context.applySnapshot(beforeParseContext);
            stack.applySnapshot(beforeRemoveFlagStack);

            return false;
        }

        int usedArguments = oldArgumentsLeft - stack.getArgumentsLeft();

        if (usedArguments != 0) {
            stack.applySnapshot(beforeParseStack);
            // Otherwise it deletes the old cursor(element to remove - 1)
            stack.next();

            for (int i = 0; i < usedArguments; i++) {
                stack.remove();
            }

        }
        return true;
    }

    public String getShortName() {
        return shortName;
    }

    public boolean allowsFullName() {
        return allowFullName;
    }

}

