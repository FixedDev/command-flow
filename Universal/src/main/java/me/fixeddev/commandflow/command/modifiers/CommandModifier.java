package me.fixeddev.commandflow.command.modifiers;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.stack.ArgumentStack;

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
