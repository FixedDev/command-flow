package me.fixeddev.commandflow.brigadier;

import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.bukkit.BukkitCommandWrapper;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.translator.Translator;
import net.kyori.adventure.text.Component;

public class BrigadierCommandWrapper extends BukkitCommandWrapper {

    public BrigadierCommandWrapper(Command command, CommandManager dispatcher, Translator translator) {
        super(command, dispatcher, translator);
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public Component getPermissionMessageComponent() {
        return permissionMessage;
    }

    public Translator getTranslator() {
        return translator;
    }

}
