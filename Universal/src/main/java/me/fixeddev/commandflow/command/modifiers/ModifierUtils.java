package me.fixeddev.commandflow.command.modifiers;

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
