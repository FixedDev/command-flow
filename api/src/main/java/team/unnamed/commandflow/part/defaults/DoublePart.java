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
import net.kyori.adventure.text.Component;

import java.util.Collections;
import java.util.List;

/**
 * A {@linkplain CommandPart} that parses one argument as a double.
 * <p>
 * This {@linkplain CommandPart} also supports having only a permitted range.
 */
public class DoublePart extends PrimitivePart {

    private final double max;
    private final double min;

    private final boolean ranged;

    /**
     * Creates a DoublePart without a range, with the specified name.
     *
     * @param name The name of this DoublePart.
     */
    public DoublePart(String name) {
        this(name, 0, 0, false);
    }

    /**
     * Creates a DoublePart with a minimum and maximum value range and a specified name.
     *
     * @param name The name of this DoublePart.
     * @param max  The maximum value(exclusive) allowed for this part.
     * @param min  The minimum value(exclusive) allowed for this part.
     */
    public DoublePart(String name, double min, double max) {
        this(name, min, max, true);
    }

    private DoublePart(String name, double min, double max, boolean ranged) {
        super(name);

        this.max = max;
        this.min = min;

        this.ranged = ranged;
    }

    @Override
    public List<Double> parseValue(CommandContext context, ArgumentStack stack, CommandPart parent) throws ArgumentParseException {
        double next = stack.nextDouble();
        if (ranged && (next > max || next < min)) {
            Component message = Component.translatable("number.out-range").args(Component.text(next), Component.text(min), Component.text(max));

            throw new ArgumentParseException(message);
        }

        return Collections.singletonList(next);
    }

}