/*
 * This file is part of commandflow, licensed under the MIT license
 *
 * Copyright (c) 2020-2023 Unnamed Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package team.unnamed.commandflow.command;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.command.modifiers.CommandModifiers;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.part.defaults.SequentialCommandPart;
import net.kyori.adventure.text.Component;
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
     * The usage of this command, if not provided it will be automatically generated from the arguments.
     *
     * @return The usage of this command in form of a {@link Component}
     */
    @Nullable
    Component getUsage();

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
     * A utility object, that has all the {@linkplain CommandModifiers} for this command,
     * and allows to easily call them at any phase of the execution of the command
     *
     * @return The {@link CommandModifiers} for this command.
     */
    @NotNull
    CommandModifiers getModifiers();

    /**
     * The {@link Action} executed after the parsing has been finished. The {@link CommandContext} passed to the action
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

        Builder usage(Component component);

        Builder permission(String permission);

        Builder permissionMessage(Component permissionMessage);

        Builder part(CommandPart part);

        Builder addParts(CommandPart... part);

        Builder addPart(CommandPart part);

        Builder modifiers(CommandModifiers modifiers);

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
