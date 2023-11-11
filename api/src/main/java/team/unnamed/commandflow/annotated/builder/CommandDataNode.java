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
package team.unnamed.commandflow.annotated.builder;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import team.unnamed.commandflow.command.Command;

import java.util.Arrays;
import java.util.List;

public interface CommandDataNode extends Buildable {

    /**
     * @param aliases The command aliases
     * @return The same {@link CommandDataNode} instance.
     * @see Command.Builder#aliases(List)
     */
    @NotNull CommandDataNode aliases(@NotNull List<String> aliases);

    /**
     * @param aliases The command aliases
     * @return The same {@link CommandDataNode} instance.
     * @see Command.Builder#aliases(String...)
     */
    @NotNull
    default CommandDataNode aliases(@NotNull String... aliases) {
        return aliases(Arrays.asList(aliases));
    }

    /**
     * @param alias The alias to add
     * @return The same {@link CommandDataNode} instance.
     * @see Command.Builder#addAlias(String)
     */
    @NotNull CommandDataNode addAlias(@NotNull String alias);

    /**
     * @param component The description component
     * @return The same {@link CommandDataNode} instance.
     * @see Command.Builder#description(Component)
     */
    @NotNull CommandDataNode description(@NotNull Component component);

    /**
     * @param component The usage component
     * @return The same {@link CommandDataNode} instance.
     * @see Command.Builder#usage(Component)
     */
    @NotNull CommandDataNode usage(@NotNull Component component);

    /**
     * @param permission The permission string for the command.
     * @return The same {@link CommandDataNode} instance.
     * @see Command.Builder#permission(String)
     */
    @NotNull CommandDataNode permission(@NotNull String permission);

    /**
     * @param permissionMessage The component to be sent when the user doesn't have permissions4.
     * @return The same {@link CommandDataNode} instance.
     * @see Command.Builder#permissionMessage(Component)
     */
    @NotNull CommandDataNode permissionMessage(@NotNull Component permissionMessage);

    /**
     * This method gives you the next step of the process of building a {@link Command}.
     *
     * @return A {@link CommandModifiersNode} instance, which will allow continuing the building process of this command.
     */
    @NotNull CommandModifiersNode modifiers();

}
