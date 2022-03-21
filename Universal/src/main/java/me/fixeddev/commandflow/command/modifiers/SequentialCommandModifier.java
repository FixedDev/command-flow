package me.fixeddev.commandflow.command.modifiers;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.stack.ArgumentStack;

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
