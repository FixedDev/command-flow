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
package team.unnamed.commandflow.annotated.part;

import team.unnamed.commandflow.part.CommandPart;

import java.lang.annotation.Annotation;
import java.util.List;

public interface PartFactory {

    /**
     * Creates a new {@link CommandPart} with the given name
     *
     * @param name      The name for the {@link CommandPart}
     * @param modifiers The modifiers for the {@link CommandPart}
     * @return A new {@link CommandPart} with the given name and using the specified modifiers
     */
    CommandPart createPart(String name, List<? extends Annotation> modifiers);

    default <T extends Annotation> T getAnnotation(List<? extends Annotation> modifiers, Class<? extends T> type) {
        T finalModifier = null;

        for (Annotation modifier : modifiers) {
            if (modifier.annotationType() == type) {
                finalModifier = (T) modifier;

                break;
            }
        }

        return finalModifier;
    }

}
