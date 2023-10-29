package me.fixeddev.commandflow.annotated.builder;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public interface CommandDataNode extends Buildable {

    /**
     * @param aliases The command aliases
     * @return The same {@link CommandDataNode} instance.
     * @see me.fixeddev.commandflow.command.Command.Builder#aliases(List)
     */
    @NotNull CommandDataNode aliases(@NotNull List<String> aliases);

    /**
     * @param aliases The command aliases
     * @return The same {@link CommandDataNode} instance.
     * @see me.fixeddev.commandflow.command.Command.Builder#aliases(String...)
     */
    @NotNull
    default CommandDataNode aliases(@NotNull String... aliases) {
        return aliases(Arrays.asList(aliases));
    }

    /**
     * @param alias The alias to add
     * @return The same {@link CommandDataNode} instance.
     * @see me.fixeddev.commandflow.command.Command.Builder#addAlias(String)
     */
    @NotNull CommandDataNode addAlias(@NotNull String alias);

    /**
     * @param component The description component
     * @return The same {@link CommandDataNode} instance.
     * @see me.fixeddev.commandflow.command.Command.Builder#description(Component)
     */
    @NotNull CommandDataNode description(@NotNull Component component);

    /**
     * @param component The usage component
     * @return The same {@link CommandDataNode} instance.
     * @see me.fixeddev.commandflow.command.Command.Builder#usage(Component)
     */
    @NotNull CommandDataNode usage(@NotNull Component component);

    /**
     * @param permission The permission string for the command.
     * @return The same {@link CommandDataNode} instance.
     * @see me.fixeddev.commandflow.command.Command.Builder#permission(String)
     */
    @NotNull CommandDataNode permission(@NotNull String permission);

    /**
     * @param permissionMessage The component to be sent when the user doesn't have permissions4.
     * @return The same {@link CommandDataNode} instance.
     * @see me.fixeddev.commandflow.command.Command.Builder#permissionMessage(Component)
     */
    @NotNull CommandDataNode permissionMessage(@NotNull Component permissionMessage);

    /**
     * This method gives you the next step of the process of building a {@link me.fixeddev.commandflow.command.Command}.
     *
     * @return A {@link CommandModifiersNode} instance, which will allow continuing the building process of this command.
     */
    @NotNull CommandModifiersNode modifiers();

}
