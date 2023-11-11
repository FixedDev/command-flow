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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An immutable blackbox containing a copy of a {@link ArgumentStack} at a specific time,
 * being able to restore the state of a {@link ArgumentStack} to this state.
 *
 * @see ArgumentStack#applySnapshot(StackSnapshot)
 */
public class StackSnapshot {

    final List<String> backing;
    final int position;

    public StackSnapshot(ArgumentStack stack, int position) {
        this.backing = new ArrayList<>(stack.getBacking());
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StackSnapshot snapshot = (StackSnapshot) o;
        return position == snapshot.position &&
                Objects.equals(backing, snapshot.backing);
    }

    @Override
    public int hashCode() {
        return Objects.hash(backing, position);
    }

}
