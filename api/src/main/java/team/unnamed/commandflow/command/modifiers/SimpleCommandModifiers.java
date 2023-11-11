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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class SimpleCommandModifiers implements CommandModifiers {

    protected final Map<ModifierPhase, CommandModifier> modifiersByPhase;

    protected SimpleCommandModifiers(Map<ModifierPhase, CommandModifier> modifiersByPhase) {
        this.modifiersByPhase = modifiersByPhase;

        Iterator<ModifierPhase> phasesIterator = modifiersByPhase.keySet().iterator();

        while (phasesIterator.hasNext()) {
            ModifierPhase phase = phasesIterator.next();

            CommandModifier value = modifiersByPhase.get(phase);

            if (value instanceof SequentialCommandModifier) {
                if (!((SequentialCommandModifier) value).hasModifiers()) {
                    phasesIterator.remove();
                }
            }
        }
    }

    @Override
    public boolean callModifiers(ModifierPhase phase, CommandContext context, ArgumentStack stack) {
        if (!hasModifiers(phase)) {
            return true;
        }

        return modifiersByPhase.get(phase).modify(context, stack, phase);
    }

    @Override
    public boolean hasModifiers(ModifierPhase phase) {
        CommandModifier modifier = modifiersByPhase.get(phase);

        if (modifier == null) {
            return false;
        }

        if (modifier instanceof SequentialCommandModifier) {
            return ((SequentialCommandModifier) modifier).hasModifiers();
        }

        return true;
    }

    protected static class Builder implements CommandModifiers.Builder {
        private final Map<ModifierPhase, CommandModifier> modifiersByPhase;

        Builder() {
            modifiersByPhase = new HashMap<>(3);

            modifiersByPhase.put(ModifierPhase.PRE_PARSE, new SequentialCommandModifier(new LinkedList<>()));
            modifiersByPhase.put(ModifierPhase.PRE_EXECUTE, new SequentialCommandModifier(new LinkedList<>()));
            modifiersByPhase.put(ModifierPhase.POST_EXECUTE, new SequentialCommandModifier(new LinkedList<>()));
        }

        @Override
        public void addModifier(CommandModifier modifier, ModifierPhase phase) {
            getSequential(phase).addModifier(modifier);
        }

        @Override
        public void addModifier(CommandModifier modifier, ModifierPhase phase, int idx) {
            getSequential(phase).addModifier(idx, modifier);
        }

        @Override
        public void removeModifier(CommandModifier modifier, ModifierPhase phase) {
            ModifierUtils.removeModifier(modifier, phase, modifiersByPhase);
        }

        @Override
        public void removeModifier(int idx, ModifierPhase phase) {
            CommandModifier phaseModifier = modifiersByPhase.get(phase);

            if (phaseModifier instanceof SequentialCommandModifier) {
                SequentialCommandModifier seqModifier = (SequentialCommandModifier) phaseModifier;

                seqModifier.removeModifier(idx);
            }
        }

        @Override
        public void setModifier(CommandModifier modifier, ModifierPhase phase) {
            modifiersByPhase.put(phase, modifier);
        }

        @Override
        public CommandModifiers build() {
            return new SimpleCommandModifiers(modifiersByPhase);
        }

        private SequentialCommandModifier getSequential(ModifierPhase phase) {
            return ModifierUtils.getSequential(phase, modifiersByPhase);
        }
    }

}
