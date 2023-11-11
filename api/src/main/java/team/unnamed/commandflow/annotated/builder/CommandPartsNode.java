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
import team.unnamed.commandflow.part.CommandPart;
import org.jetbrains.annotations.NotNull;
import team.unnamed.commandflow.annotated.part.PartFactory;
import team.unnamed.commandflow.annotated.part.PartInjector;
import team.unnamed.commandflow.annotated.part.PartModifier;
import team.unnamed.commandflow.command.Command;

import java.lang.reflect.Method;

public interface CommandPartsNode extends Buildable {

    /**
     * Sets the {@link CommandPart} list of this command to a one created based on a {@link Method} with annotations.
     * The actual {@link CommandPart} instances will be created based on the type and annotations that the parameter has,
     * those are created with a {@link PartFactory} and after that modified by a {@link PartModifier}
     * which are returned by the {@link PartInjector} used on the builder.
     *
     * @param method  The method which parameters will be converted to the list of {@link CommandPart} instances of the comand.
     * @param handler The {@link CommandClass} instance in which this method is present.
     * @return A {@link CommandActionNode} instance, which will allow continuing the building process of this command.
     */
    @NotNull CommandActionNode ofMethodParameters(@NotNull Method method,
                                                  @NotNull CommandClass handler);

    /**
     * Adds a {@link CommandPart} into the {@link Command} being built.
     *
     * @param part The {@link CommandPart} to add into the {@link Command} parts.
     * @return A {@link CommandActionNode} instance, which will allow continuing the building process of this command.
     * @see Command.Builder#addPart(CommandPart)
     */
    @NotNull CommandPartsNode addPart(@NotNull CommandPart part);

    /**
     * This method gives you the next step of the process of building a {@link Command}.
     *
     * @return A {@link CommandActionNode} instance, which will allow continuing the building process of this command.
     */
    @NotNull CommandActionNode action();

}
