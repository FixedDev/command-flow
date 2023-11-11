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
import team.unnamed.commandflow.exception.CommandException;
import team.unnamed.commandflow.exception.NoMoreArgumentsException;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.part.PartsWrapper;
import team.unnamed.commandflow.part.SinglePartWrapper;
import team.unnamed.commandflow.stack.ArgumentStack;
import team.unnamed.commandflow.stack.SimpleArgumentStack;
import team.unnamed.commandflow.stack.StackSnapshot;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OptionalPart implements CommandPart, SinglePartWrapper {

    private final CommandPart part;
    private final List<String> defaultValues;
    private final boolean considerInvalidAsEmpty;

    public OptionalPart(CommandPart part) {
        this(part, true);
    }

    public OptionalPart(CommandPart part, boolean considerInvalidAsEmpty) {
        this(part, considerInvalidAsEmpty, new ArrayList<>());
    }

    public OptionalPart(CommandPart part, List<String> defaultValues) {
        this(part, false, defaultValues);
    }

    public OptionalPart(CommandPart part, boolean considerInvalidAsEmpty, List<String> defaultValues) {
        this.part = part;
        this.defaultValues = defaultValues;
        this.considerInvalidAsEmpty = considerInvalidAsEmpty;
    }

    @Override
    public String getName() {
        return part.getName() + "-optional";
    }

    @Override
    public @Nullable Component getLineRepresentation() {
        Component partLineRepresent = part.getLineRepresentation();

        if (partLineRepresent == null) {
            return null;
        }
        return Component.text()
                .append(Component.text("["))
                .append(partLineRepresent)
                .append(Component.text("]"))
                .build();
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack, CommandPart caller) throws ArgumentParseException {
        StackSnapshot snapshot = stack.getSnapshot();
        ContextSnapshot contextSnapshot = context.getSnapshot();

        try {
            part.parse(context, stack, caller);
        } catch (ArgumentParseException | NoMoreArgumentsException e) {
            if (shouldRewind(caller, e)) {
                throw e;
            }

            stack.applySnapshot(snapshot);
            context.applySnapshot(contextSnapshot);

            if (!defaultValues.isEmpty()) {
                try {
                    part.parse(context, new SimpleArgumentStack(defaultValues), this);
                } catch (ArgumentParseException | NoMoreArgumentsException ignored) {
                }
            }
        }
    }

    private boolean shouldRewind(CommandPart caller, CommandException e) {
        boolean isLast = true;

        if (caller instanceof PartsWrapper) {
            List<CommandPart> parts = ((PartsWrapper) caller).getParts();

            isLast = parts.indexOf(this) == parts.size() - 1;
        }

        return isLast && !(e instanceof NoMoreArgumentsException) && !considerInvalidAsEmpty;
    }

    @Override
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        return Collections.emptyList();
    }

    @Override
    public boolean isAsync() {
        return part.isAsync();
    }

    @Override
    public CommandPart getPart() {
        return part;
    }

    public List<String> getDefaultValues() {
        return defaultValues;
    }

}
