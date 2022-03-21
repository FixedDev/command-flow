package me.fixeddev.commandflow.command.modifiers;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.stack.ArgumentStack;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * This class is used to store all the modifiers of a command, and aid in the process of calling them in the right order at
 * any phase of the execution.
 * <p>
 * It's not assured to be thread-safe, the only guarantee being made is that it's not modified after being created.
 */
public interface CommandModifiers {

    CommandModifiers EMPTY = new CommandModifiers() {
        @Override
        public boolean callModifiers(ModifierPhase phase, CommandContext context, ArgumentStack stack) {
            return true;
        }

        @Override
        public boolean hasModifiers(ModifierPhase phase) {
            return false;
        }
    };

    /**
     * Calls the modifiers for a specified phase in the order specified when creating this object.
     *
     * @param phase   The phase to call the modifiers for.
     * @param context The context of the command.
     * @param stack   The stack of arguments.
     * @return If the execution of this phase should continue.
     */
    boolean callModifiers(ModifierPhase phase, CommandContext context, ArgumentStack stack);

    /**
     * @param phase the phase to check for.
     * @return If the command has any modifiers at a specified phase.
     */
    boolean hasModifiers(ModifierPhase phase);

    /**
     * Creates a new builder to create a {@link CommandModifiers} object.
     *
     * @return A new builder.
     */
    static Builder builder() {
        return new SimpleCommandModifiers.Builder();
    }

    interface Builder {
        /**
         * Adds a new modifier at the end of the list of modifiers to the specified phase.
         * <p>
         * If the modifier already set for the phase isn't a {@linkplain SequentialCommandModifier} then the
         * modifier will removed, a {@linkplain SequentialCommandModifier} will be created and the old modifier will be added
         * first.
         *
         * @param modifier The modifier to add.
         * @param phase    The phase to add the modifier to.
         */
        void addModifier(CommandModifier modifier, ModifierPhase phase);

        /**
         * Adds a new modifier at specified index of the list of modifiers to the specified phase.
         * <p>
         * If the modifier already set for the phase isn't a {@linkplain SequentialCommandModifier} then the
         * modifier will removed, a {@linkplain SequentialCommandModifier} will be created and the old modifier will be added
         * first.
         *
         * @param modifier The modifier to add.
         * @param phase    The phase to add the modifier to.
         * @param idx      The index to add the modifier at.
         */
        void addModifier(CommandModifier modifier, ModifierPhase phase, int idx);

        /**
         * Removes the specified modifier from the list of modifiers, if it exists.
         * <p>
         * If the modifier being removed is the parent one, then it will be replaced with a {@linkplain SequentialCommandModifier}.
         *
         * @param modifier The modifier to remove.
         * @param phase    The phase to remove the modifier from.
         */
        void removeModifier(CommandModifier modifier, ModifierPhase phase);

        /**
         * Removes the modifier from the specified index from the list of modifiers, if it exists.
         * <p>
         * If the parent modifier isn't a {@linkplain SequentialCommandModifier} then this method will do nothing.
         *
         * @param idx   The index of the modifier to remove.
         * @param phase The phase to remove the modifier from.
         */
        void removeModifier(int idx, ModifierPhase phase);

        /**
         * Sets the main modifier of the specified phase.
         *
         * @param modifier The modifier to set.
         * @param phase    The phase to set the modifier to.
         */
        void setModifier(CommandModifier modifier, ModifierPhase phase);

        CommandModifiers build();
    }
}
