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

public class FloatPart extends PrimitivePart {

    private final float max;
    private final float min;

    private final boolean ranged;

    public FloatPart(String name) {
        this(name, 0, 0, false);
    }

    public FloatPart(String name, float min, float max) {
        this(name, min, max, true);
    }

    private FloatPart(String name, float min, float max, boolean ranged) {
        super(name);

        this.max = max;
        this.min = min;

        this.ranged = ranged;
    }

    @Override
    public List<Float> parseValue(CommandContext context, ArgumentStack stack, CommandPart parent) throws ArgumentParseException {
        float next = stack.nextFloat();
        if (ranged && (next > max || next < min)) {
            Component message = Component.translatable("number.out-range").args(Component.text(next), Component.text(min), Component.text(max));

            throw new ArgumentParseException(message);
        }

        return Collections.singletonList(next);
    }

}