package me.fixeddev.commandflow.command.modifiers;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.stack.ArgumentStack;

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
