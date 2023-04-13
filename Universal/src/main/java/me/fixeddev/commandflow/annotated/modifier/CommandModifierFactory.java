package me.fixeddev.commandflow.annotated.modifier;

import me.fixeddev.commandflow.command.modifiers.CommandModifier;
import me.fixeddev.commandflow.command.modifiers.CommandModifiers;

import java.lang.annotation.Annotation;
import java.util.List;

public interface CommandModifierFactory {
    /**
     * Creates and adds one or more {@linkplain CommandModifier}'s to the {@link CommandModifiers.Builder}
     *
     * @param builder     The builder to add the modifiers to.
     * @param annotations The annotations at the context (command class or method)
     * @return The CommandModifier added into the specified builder.
     */
    CommandModifier modify(CommandModifiers.Builder builder, List<? extends Annotation> annotations);
}
