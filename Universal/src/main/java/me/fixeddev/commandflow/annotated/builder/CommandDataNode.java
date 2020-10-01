package me.fixeddev.commandflow.annotated.builder;

import net.kyori.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public interface CommandDataNode extends Buildable {
    /**
     * @return The same {@link CommandDataNode} instance.
     * @see me.fixeddev.commandflow.command.Command.Builder#aliases(List)
     */
    @NotNull CommandDataNode aliases(@NotNull List<String> aliases);

    /**
     * @return The same {@link CommandDataNode} instance.
     * @see me.fixeddev.commandflow.command.Command.Builder#aliases(String...)
     */
    @NotNull
    default CommandDataNode aliases(@NotNull String... aliases) {
        return aliases(Arrays.asList(aliases));
    }

    /**
     * @return The same {@link CommandDataNode} instance.
     * @see me.fixeddev.commandflow.command.Command.Builder#addAlias(String)
     */
    @NotNull CommandDataNode addAlias(@NotNull String alias);

    /**
     * @return The same {@link CommandDataNode} instance.
     * @see me.fixeddev.commandflow.command.Command.Builder#description(Component)
     */
    @NotNull CommandDataNode description(@NotNull Component component);

    /**
     * @return The same {@link CommandDataNode} instance.
     * @see me.fixeddev.commandflow.command.Command.Builder#permission(String)
     */
    @NotNull CommandDataNode permission(@NotNull String permission);

    /**
     * @return The same {@link CommandDataNode} instance.
     * @see me.fixeddev.commandflow.command.Command.Builder#permissionMessage(Component)
     */
    @NotNull CommandDataNode permissionMessage(@NotNull Component permissionMessage);

    /**
     * This method gives you the next step of the process of building a {@link me.fixeddev.commandflow.command.Command}.
     *
     * @return A {@link CommandPartsNode} instance, which will allow continuing the building process of this command.
     */
    @NotNull CommandPartsNode parts();
}
