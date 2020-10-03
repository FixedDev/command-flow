package me.fixeddev.commandflow.annotated.builder;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.part.CommandPart;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

public interface CommandPartsNode extends Buildable {
    /**
     * Sets the {@link CommandPart} list of this command to a one created based on a {@link Method} with annotations.
     * The actual {@link CommandPart} instances will be created based on the type and annotations that the parameter has,
     * those are created with a {@link me.fixeddev.commandflow.annotated.part.PartFactory} and after that modified by a {@link me.fixeddev.commandflow.annotated.part.PartModifier}
     * which are returned by the {@link me.fixeddev.commandflow.annotated.part.PartInjector} used on the builder.
     *
     * @param method  The method which parameters will be converted to the list of {@link CommandPart} instances of the comand.
     * @param handler The {@link CommandClass} instance in which this method is present.
     * @return A {@link CommandActionNode} instance, which will allow continuing the building process of this command.
     */
    @NotNull CommandActionNode ofMethodParameters(@NotNull Method method,
                                                  @NotNull CommandClass handler);

    /**
     * Adds a {@link CommandPart} into the {@link me.fixeddev.commandflow.command.Command} being built.
     *
     * @param part The {@link CommandPart} to add into the {@link me.fixeddev.commandflow.command.Command} parts.
     * @return A {@link CommandActionNode} instance, which will allow continuing the building process of this command.
     * @see me.fixeddev.commandflow.command.Command.Builder#addPart(CommandPart)
     */
    @NotNull CommandPartsNode addPart(@NotNull CommandPart part);

    /**
     * This method gives you the next step of the process of building a {@link me.fixeddev.commandflow.command.Command}.
     *
     * @return A {@link CommandActionNode} instance, which will allow continuing the building process of this command.
     */
    @NotNull CommandActionNode action();
}
