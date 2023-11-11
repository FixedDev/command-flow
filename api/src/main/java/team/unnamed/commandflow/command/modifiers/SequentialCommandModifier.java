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

import java.util.List;

public class SequentialCommandModifier implements CommandModifier {

    private final List<CommandModifier> modifierList;

    public SequentialCommandModifier(List<CommandModifier> modifierList) {
        this.modifierList = modifierList;
    }

    @Override
    public boolean modify(CommandContext context, ArgumentStack stack, ModifierPhase phase) {
        for (CommandModifier modifier : modifierList) {
            if (!modifier.modify(context, stack, phase)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Adds a modifier to the end of the list of modifiers for this SequentialCommandModifier.
     *
     * @param modifier The modifier to add.
     */
    public void addModifier(CommandModifier modifier) {
        modifierList.add(modifier);
    }

    /**
     * Adds a modifier at the specified index of the list of modifiers for this SequentialCommandModifier.
     *
     * @param idx      The index where the modifier should be added.
     * @param modifier The modifier to add.
     */
    public void addModifier(int idx, CommandModifier modifier) {
        modifierList.add(idx, modifier);
    }

    /**
     * Removes the modifier at the specified index from the list of modifiers for this SequentialCommandModifier.
     *
     * @param idx The index to remove this modifier from.
     */
    public void removeModifier(int idx) {
        modifierList.remove(idx);
    }

    /**
     * Removes a modifier from the list of modifiers for this SequentialCommandModifier.
     *
     * @param modifier The modifier to remove.
     */
    public void removeModifier(CommandModifier modifier) {
        modifierList.remove(modifier);
    }

    public boolean hasModifiers() {
        return !modifierList.isEmpty();
    }

}
