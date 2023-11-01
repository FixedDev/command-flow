package team.unnamed.commandflow.annotated.builder;

import team.unnamed.commandflow.annotated.CommandClass;
import team.unnamed.commandflow.command.modifiers.CommandModifier;
import team.unnamed.commandflow.command.modifiers.ModifierPhase;
import team.unnamed.commandflow.part.CommandPart;
import org.jetbrains.annotations.NotNull;
import team.unnamed.commandflow.annotated.modifier.CommandModifierFactory;
import team.unnamed.commandflow.command.Command;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

public interface CommandModifiersNode extends Buildable {

    /**
     * Sets the {@link CommandModifier} list of this command to a one created based on a {@link Method} with annotations.
     * The actual {@link CommandModifier} instances will be created based on the annotations that the method has, those
     * are created using a {@link CommandModifierFactory} provided by the PartInjector.
     *
     * @param method  The method which parameters will be converted to the list of {@link CommandPart} instances of the comand.
     * @param handler The {@link CommandClass} instance in which this method is present.
     * @return A {@link CommandModifiersNode} instance, which will allow continuing the building process of this command.
     */
    @NotNull CommandModifiersNode ofMethod(@NotNull Method method,
                                           @NotNull CommandClass handler);

    @NotNull CommandModifiersNode addModifiers(List<Annotation> annotations);


    @NotNull CommandModifiersNode addModifier(@NotNull ModifierPhase phase, @NotNull CommandModifier modifier);

    /**
     * This method gives you the next step of the process of building a {@link Command}.
     *
     * @return A {@link CommandPartsNode} instance, which will allow continuing the building process of this command.
     */
    @NotNull CommandPartsNode parts();
}
