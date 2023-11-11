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
package team.unnamed.commandflow.annotated.builder;

import team.unnamed.commandflow.annotated.CommandClass;
import team.unnamed.commandflow.command.modifiers.CommandModifier;
import team.unnamed.commandflow.command.modifiers.ModifierPhase;
import team.unnamed.commandflow.part.CommandPart;
import org.jetbrains.annotations.NotNull;
import team.unnamed.commandflow.annotated.modifier.CommandModifierFactory;
import team.unnamed.commandflow.command.Command;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

public interface CommandModifiersNode extends Buildable {

    /**
     * Sets the {@link CommandModifier} list of this command to a one created based on a {@link Method} with annotations.
     * The actual {@link CommandModifier} instances will be created based on the annotations that the method has, those
     * are created using a {@link CommandModifierFactory} provided by the PartInjector.
     *
     * @param method  The method which parameters will be converted to the list of {@link CommandPart} instances of the comand.
     * @param handler The {@link CommandClass} instance in which this method is present.
     * @return A {@link CommandModifiersNode} instance, which will allow continuing the building process of this command.
     */
    @NotNull CommandModifiersNode ofMethod(@NotNull Method method,
                                           @NotNull CommandClass handler);

    @NotNull CommandModifiersNode addModifiers(List<Annotation> annotations);


    @NotNull CommandModifiersNode addModifier(@NotNull ModifierPhase phase, @NotNull CommandModifier modifier);

    /**
     * This method gives you the next step of the process of building a {@link Command}.
     *
     * @return A {@link CommandPartsNode} instance, which will allow continuing the building process of this command.
     */
    @NotNull CommandPartsNode parts();
}
