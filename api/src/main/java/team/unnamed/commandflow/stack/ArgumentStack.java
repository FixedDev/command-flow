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
package team.unnamed.commandflow.stack;

import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.exception.NoMoreArgumentsException;

import java.util.List;

public interface ArgumentStack {

    boolean hasNext();

    String next() throws NoMoreArgumentsException;

    String peek() throws NoMoreArgumentsException;

    String current();

    String remove();

    int getPosition();

    int getSize();

    int getArgumentsLeft();

    String nextQuoted();

    int nextInt() throws ArgumentParseException;

    float nextFloat() throws ArgumentParseException;

    double nextDouble() throws ArgumentParseException;

    byte nextByte() throws ArgumentParseException;

    boolean nextBoolean() throws ArgumentParseException;

    long nextLong() throws ArgumentParseException;

    void markAsConsumed();

    List<String> getBacking();

    ArgumentStack getSlice(int start, int end);

    default ArgumentStack getSliceFrom(int start) {
        return getSlice(start, getSize());
    }

    default ArgumentStack getSliceTo(int end) {
        return getSlice(getPosition(), end);
    }

    default ArgumentStack getSlice(int size) {
        return getSliceTo(getPosition() + size);
    }

    default StackSnapshot getSnapshot() {
        return getSnapshot(true);
    }

    StackSnapshot getSnapshot(boolean useCurrentPos);

    default void applySnapshot(StackSnapshot snapshot) {
        applySnapshot(snapshot, true);
    }

    void applySnapshot(StackSnapshot snapshot, boolean changeArgs);

}
