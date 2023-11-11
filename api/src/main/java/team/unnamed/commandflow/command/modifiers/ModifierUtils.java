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

import java.util.LinkedList;
import java.util.Map;

class ModifierUtils {

    public static void removeModifier(CommandModifier modifier, ModifierPhase phase, Map<ModifierPhase, CommandModifier> modifiersByPhase) {
        if (!modifiersByPhase.remove(phase, modifier)) {
            CommandModifier phaseModifier = modifiersByPhase.get(phase);

            if (phaseModifier instanceof SequentialCommandModifier) {
                SequentialCommandModifier seqModifier = (SequentialCommandModifier) phaseModifier;

                seqModifier.removeModifier(modifier);
            }
        } else {
            modifiersByPhase.put(phase, new SequentialCommandModifier(new LinkedList<>()));
        }
    }

    public static SequentialCommandModifier getSequential(ModifierPhase phase, Map<ModifierPhase, CommandModifier> modifiersByPhase) {
        CommandModifier modifier = modifiersByPhase.get(phase);

        if (!(modifier instanceof SequentialCommandModifier)) {
            SequentialCommandModifier seqModifier = new SequentialCommandModifier(new LinkedList<>());
            seqModifier.addModifier(modifier);

            modifiersByPhase.put(phase, seqModifier);

            return seqModifier;
        }

        return (SequentialCommandModifier) modifier;
    }

}
