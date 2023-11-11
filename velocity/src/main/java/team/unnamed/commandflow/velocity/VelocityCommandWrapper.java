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
package team.unnamed.commandflow.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.RawCommand;
import team.unnamed.commandflow.CommandManager;
import team.unnamed.commandflow.Namespace;
import team.unnamed.commandflow.command.Command;
import team.unnamed.commandflow.exception.CommandException;
import team.unnamed.commandflow.translator.Translator;
import net.kyori.adventure.text.Component;

import java.util.List;

public class VelocityCommandWrapper implements RawCommand {

    protected final CommandManager commandManager;
    protected final Translator translator;

    protected final String[] aliases;
    protected final String permission;
    protected final String command;

    public VelocityCommandWrapper(Command command, CommandManager commandManager,
                                  Translator translator) {

        this.command = command.getName();
        this.commandManager = commandManager;
        this.translator = translator;

        this.aliases = command.getAliases().toArray(new String[0]);
        this.permission = command.getPermission();
    }


    @Override
    public void execute(Invocation invocation) {
        CommandSource commandSource = invocation.source();
        String argumentLine = invocation.alias() + " " + invocation.arguments();

        Namespace namespace = Namespace.create();
        namespace.setObject(CommandSource.class, VelocityCommandManager.SENDER_NAMESPACE, commandSource);
        namespace.setObject(String.class, "label", invocation.alias());

        try {
            commandManager.execute(namespace, argumentLine);
        } catch (CommandException e) {
            CommandException exceptionToSend = e;

            if (e.getCause() instanceof CommandException) {
                exceptionToSend = (CommandException) e.getCause();
            }

            sendMessageToSender(e, namespace);

            throw new CommandException("An unexpected exception occurred while executing the command " + this.command, exceptionToSend);
        }
    }

    public String getPermission() {
        return permission;
    }

    public String[] getAliases() {
        return aliases;
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        CommandSource commandSource = invocation.source();
        String argumentLine = invocation.alias() + " "+ invocation.arguments();

        Namespace namespace = Namespace.create();
        namespace.setObject(CommandSource.class, VelocityCommandManager.SENDER_NAMESPACE, commandSource);

        return commandManager.getSuggestions(namespace, argumentLine);
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        CommandSource commandSource = invocation.source();

        Namespace namespace = Namespace.create();
        namespace.setObject(CommandSource.class, VelocityCommandManager.SENDER_NAMESPACE, commandSource);

        return commandManager.getAuthorizer().isAuthorized(namespace, getPermission());
    }

    protected static void sendMessageToSender(CommandException exception, Namespace namespace) {
        CommandManager commandManager = namespace.getObject(CommandManager.class, "commandManager");
        CommandSource sender = namespace.getObject(CommandSource.class, VelocityCommandManager.SENDER_NAMESPACE);

        Component component = exception.getMessageComponent();
        Component translatedComponent = commandManager.getTranslator().translate(component, namespace);

        sender.sendMessage(translatedComponent);
    }
}
