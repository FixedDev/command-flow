package team.unnamed.commandflow.annotated.builder;

import team.unnamed.commandflow.annotated.CommandClass;
import team.unnamed.commandflow.command.Action;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

public interface CommandActionNode extends Buildable {

    /**
     * Sets the {@link Action} of this command to a one created based on a {@link Method} with annotations, actually
     * delegating the {@link Action} setting to {@link CommandActionNode#action(Action)}.
     *
     * @param method       The method which will be the {@link Action}.
     * @param commandClass The {@link CommandClass} instance in which this method is present.
     * @return A {@link SubCommandsNode} instance, which will allow continuing the building process of this command.
     */
    @NotNull SubCommandsNode action(@NotNull Method method,
                                    @NotNull CommandClass commandClass);

    /**
     * Sets the {@link Action} for this command to a given one.
     *
     * @param action The {@link Action} for this command.
     * @return A {@link SubCommandsNode} instance, which will allow continuing the building process of this command.
     */
    @NotNull SubCommandsNode action(@NotNull Action action);

}
