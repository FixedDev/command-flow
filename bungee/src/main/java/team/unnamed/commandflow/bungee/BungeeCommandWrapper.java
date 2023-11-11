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
package team.unnamed.commandflow.bungee;

import team.unnamed.commandflow.CommandManager;
import team.unnamed.commandflow.Namespace;
import team.unnamed.commandflow.exception.CommandException;
import team.unnamed.commandflow.translator.Translator;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BungeeCommandWrapper extends Command implements TabExecutor {

    protected final CommandManager commandManager;
    protected final Translator translator;

    protected final String[] aliases;
    protected final String permission;

    public BungeeCommandWrapper(team.unnamed.commandflow.command.Command command, CommandManager commandManager,
                                Translator translator) {
        super(command.getName());

        this.commandManager = commandManager;
        this.translator = translator;

        this.aliases = command.getAliases().toArray(new String[0]);
        this.permission = command.getPermission();
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        List<String> argumentLine = new ArrayList<>();
        argumentLine.add(getName());
        argumentLine.addAll(Arrays.asList(args));

        Namespace namespace = Namespace.create();
        namespace.setObject(CommandSender.class, BungeeCommandManager.SENDER_NAMESPACE, commandSender);
        namespace.setObject(String.class, "label", getName());

        try {
            commandManager.execute(namespace, argumentLine);
        } catch (CommandException e) {
            CommandException exceptionToSend = e;

            if (e.getCause() instanceof CommandException) {
                exceptionToSend = (CommandException) e.getCause();
            }

            sendMessageToSender(exceptionToSend, namespace);

            throw new CommandException("An unexpected exception occurred while executing the command " + getName(), exceptionToSend.getCause() != null ? exceptionToSend.getCause() : exceptionToSend);
        }
    }

    public String getPermission() {
        return permission;
    }

    public String[] getAliases() {
        return aliases;
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        List<String> argumentList = new ArrayList<>(Arrays.asList(strings));
        argumentList.add(0, getName());

        Namespace namespace = Namespace.create();
        namespace.setObject(CommandSender.class, BungeeCommandManager.SENDER_NAMESPACE, commandSender);

        return commandManager.getSuggestions(namespace, argumentList);
    }

    protected static void sendMessageToSender(CommandException exception, Namespace namespace) {
        CommandManager commandManager = namespace.getObject(CommandManager.class, "commandManager");
        CommandSender sender = namespace.getObject(CommandSender.class, BungeeCommandManager.SENDER_NAMESPACE);

        Component component = exception.getMessageComponent();
        Component translatedComponent = commandManager.getTranslator().translate(component, namespace);

        BaseComponent[] components = MessageUtils.kyoriToBungee(translatedComponent);

        sender.sendMessage(components);
    }

}
