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
package team.unnamed.commandflow.command.modifiers;

/**
 * This enum represents the different phases in which a modifier may be applied.
 */
public enum ModifierPhase {
    /**
     * The modifier is applied before the command is parsed, just after the context is created.
     * <p>
     * Only in this phase the stack is available, in the next phases the stack doesn't exists so it's set to null.
     */
    PRE_PARSE,
    /**
     * The modifier is applied after the command is parsed, just before the command is executed.
     * <p>
     * Another name for this could be POST_PARSE.
     */
    PRE_EXECUTE,
    /**
     * The modifier is applied after the command is executed.
     * <p>
     * This phase can't cancel anything, as it is the last phase, and the command is already executed, trying to cancel
     * something is useless, and only will prevent the next modifier from being called.
     */
    POST_EXECUTE
}
