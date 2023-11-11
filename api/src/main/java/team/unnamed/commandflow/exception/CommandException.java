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
package team.unnamed.commandflow.exception;

import team.unnamed.commandflow.command.Command;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;

public class CommandException extends RuntimeException {

    private Component message;

    private Command command;

    public CommandException() {
    }

    public CommandException(Component message) {
        this((message instanceof TextComponent) ? ((TextComponent) message).content() :
                ((message instanceof TranslatableComponent) ? "%translatable: " + ((TranslatableComponent) message).key() + "%" : null));

        this.message = message;
    }

    public CommandException(String message) {
        super(message);

        if (message == null) {
            this.message = null;

            return;
        }

        if (message.startsWith("%translatable:") && message.endsWith("%")) {
            this.message = Component.translatable(message.substring(14, message.length() - 1));
        } else {
            this.message = Component.text(message);
        }
    }

    public CommandException(String message, Throwable cause) {
        super(message, cause);

        if (message.startsWith("%translatable:") && message.endsWith("%")) {
            this.message = Component.translatable(message.substring(14, message.length() - 1));
        } else {
            this.message = Component.text(message);
        }
    }

    public CommandException(Throwable cause) {
        super(cause);
    }

    public CommandException setCommand(Command argument) {
        this.command = argument;

        return this;
    }

    public Command getCommand() {
        return command;
    }

    public Component getMessageComponent() {
        return message;
    }

}
