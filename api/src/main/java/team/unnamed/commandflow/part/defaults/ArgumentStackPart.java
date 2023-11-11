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
import team.unnamed.commandflow.part.ArgumentPart;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.stack.ArgumentStack;
import team.unnamed.commandflow.stack.SimpleArgumentStack;
import team.unnamed.commandflow.stack.StackSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A {@linkplain CommandPart} that provides direct access to the remaining {@linkplain ArgumentStack}.
 * <p>
 * Actually this shouldn't be an {@linkplain ArgumentPart} since it isn't using the arguments directly, but this is the best way to represent it.
 */
public class ArgumentStackPart implements ArgumentPart {

    private final String name;

    public ArgumentStackPart(String name) {
        this.name = name;
    }

    @Override
    public List<ArgumentStack> parseValue(CommandContext context, ArgumentStack stack, CommandPart parent) throws ArgumentParseException {
        StackSnapshot snapshot = stack.getSnapshot();
        stack.markAsConsumed();

        ArgumentStack newStack = new SimpleArgumentStack(new ArrayList<>());
        newStack.applySnapshot(snapshot, true);

        return Collections.singletonList(newStack);
    }

    @Override
    public String getName() {
        return name;
    }

}
