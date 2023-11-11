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
package team.unnamed.commandflow.annotated.action;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.part.CommandPart;

public interface ValueGetter {
    Object getValue(CommandContext commandContext);

    static ValueGetter forPart(CommandPart part) {
        return commandContext ->
                commandContext.getValue(part).orElse(null);
    }

    static ValueGetter forPart(String partName, int index) {
        return commandContext ->
                commandContext.getPart(partName, index)
                        .flatMap(commandContext::getValue).orElse(null);
    }

    static ValueGetter forOptionalPart(CommandPart part) {
        return commandContext ->
                commandContext.getValue(part);
    }

    static ValueGetter forOptionalPart(String partName, int index) {
        return commandContext ->
                commandContext.getPart(partName, index)
                        .flatMap(commandContext::getValue);
    }

    static ValueGetter forPartValues(CommandPart part) {
        return commandContext ->
                commandContext.getValues(part);
    }

    static ValueGetter forPartValues(String partName, int index) {
        return commandContext ->
                commandContext.getPart(partName, index)
                        .map(commandContext::getValues);
    }

    static ValueGetter forPartRaw(CommandPart part) {
        return commandContext ->
                commandContext.getRaw(part);
    }

    static ValueGetter ofInstance(Object object) {
        return commandContext -> object;
    }
}
