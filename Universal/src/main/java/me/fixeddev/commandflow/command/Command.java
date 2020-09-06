package me.fixeddev.commandflow.command;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.part.defaults.SequentialCommandPart;
import net.kyori.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public interface Command {
    /**
     * The main name for this command used to identify the command internally.
     *
     * @return The principal name of this command.
     */
    @NotNull
    String getName();

    /**
     * Other secondary names(aliases) for this command used when executing the command or
     * searching for a matching command.
     *
     * @return A list of aliases for this command.
     */
    @NotNull
    List<String> getAliases();

    /**
     * The description of the command.
     *
     * @return The description of the command in form of a {@link Component}.
     */
    @Nullable
    Component getDescription();

    /**
     * The permission needed to execute this command.
     *
     * @return A string representing the permission required to execute this command, or empty/null to represent no permission.
     */
    @Nullable
    String getPermission();

    /**
     * The message send to the executor when it doesn't has the permission returned by {@link Command#getPermission()}
     *
     * @return The message for the executor when unauthorized to execute this command in form of a {@link Component}.
     */
    @Nullable
    Component getPermissionMessage();

    /**
     * The principal {@link CommandPart} for this command, generally an instance of {@link SequentialCommandPart} but may be an instance of any {@link CommandPart} instance.
     * The parsing of the command is passed directly to this part without any extra modifications.
     *
     * @return The main {@link CommandPart} that parses the command arguments.
     */
    @NotNull
    CommandPart getPart();

    /**
     * The {@link Action} executed after the parsing has been finished. The {@link me.fixeddev.commandflow.CommandContext} passed to the action
     * is the resulting one after the parsing by the {@link CommandPart} returned by {@link Command#getPart()} is done.
     *
     * @return The {@link Action} for this command.
     */
    @NotNull
    Action getAction();

    interface Builder {
        Builder aliases(List<String> aliases);

        default Builder aliases(String... aliases) {
            return aliases(Arrays.asList(aliases));
        }

        Builder addAlias(String alias);

        Builder description(Component component);

        Builder permission(String permission);

        Builder permissionMessage(Component permissionMessage);

        Builder part(CommandPart part);

        Builder addParts(CommandPart... part);

        Builder addPart(CommandPart part);

        Builder action(Action action);

        default Builder action(Consumer<CommandContext> action) {
            return action((context) -> {
                action.accept(context);

                return true;
            });
        }

        Command build();
    }

    static Builder builder(String name) {
        return new SimpleCommand.Builder(name);
    }
}
