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
package team.unnamed.commandflow.usage;

import team.unnamed.commandflow.CommandContext;
import net.kyori.adventure.text.Component;
import team.unnamed.commandflow.translator.Translator;

/**
 * Creates a usage based on the CommandContext that you give to this class.
 * This class should be stateless, that means that it shouldn't have a defined state at any moment.
 */
public interface UsageBuilder {

    /**
     * Gets a usage based on the given {@link CommandContext}.
     *
     * @param commandContext The {@link CommandContext} used to create a usage.
     * @return A {@link Component} containing the usage for this command, shouldn't be sent directly to the
     * any executor without being translated first by a {@link Translator}.
     */
    Component getUsage(CommandContext commandContext);

}
