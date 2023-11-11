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
package team.unnamed.commandflow.command.modifiers;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.stack.ArgumentStack;

public interface CommandModifier {

    /**
     * Tries to intercept the command in a certain phase of its execution, allowing it to cancel the execution.
     * This allows us to implement things like cooldowns, or other things that modify the command.
     *
     * @param context The {@link CommandContext} for the command, at least the command to start parsing/executing is set.
     * @param stack The {@link ArgumentStack} at its current position without any modification.
     * @param phase The {@link ModifierPhase} in which this modifier is being called.
     * @return Whether the command should continue being parsed/executed or not.
     */
    boolean modify(CommandContext context, ArgumentStack stack, ModifierPhase phase);

}
