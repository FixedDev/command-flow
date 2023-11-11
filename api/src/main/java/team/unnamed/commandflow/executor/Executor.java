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
package team.unnamed.commandflow.executor;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.command.Action;
import team.unnamed.commandflow.command.Command;
import team.unnamed.commandflow.exception.CommandException;
import team.unnamed.commandflow.usage.UsageBuilder;

/**
 * This class has the functionality of actually calling the {@link Action} of the command
 * based on the given {@link CommandContext}.
 */
public interface Executor {

    /**
     * Executes the right {@link Action} based on the given {@link CommandContext}.
     *
     * @param commandContext The {@link CommandContext} of the {@link Command} to execute.
     * @param builder        The {@link UsageBuilder} used to generate an usage text if the {@link Action} returns false or if
     *                       the command execution fails for a valid reason.
     * @return If the {@link Action} for the given {@link CommandContext} could be executed.
     * @throws CommandException If the {@link Action} throws an exception.
     */
    boolean execute(CommandContext commandContext, UsageBuilder builder) throws CommandException;

}
