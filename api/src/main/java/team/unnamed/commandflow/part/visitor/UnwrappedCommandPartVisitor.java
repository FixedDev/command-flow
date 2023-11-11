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
package team.unnamed.commandflow.part.visitor;

import team.unnamed.commandflow.part.PartsWrapper;
import team.unnamed.commandflow.part.defaults.FirstMatchPart;
import team.unnamed.commandflow.part.defaults.SequentialCommandPart;

public interface UnwrappedCommandPartVisitor<R> extends CommandPartVisitor<R> {

    @Override
    default R visit(PartsWrapper partsWrapper) {
        if (partsWrapper instanceof SequentialCommandPart) {
            return visitSequential((SequentialCommandPart) partsWrapper);
        }

        if (partsWrapper instanceof FirstMatchPart) {
            return visitFirstMatch((FirstMatchPart) partsWrapper);
        }

        return visitWrapper(partsWrapper);
    }

    R visitWrapper(PartsWrapper partsWrapper);

    R visitSequential(SequentialCommandPart sequentialCommandPart);

    R visitFirstMatch(FirstMatchPart firstMatchPart);

}
