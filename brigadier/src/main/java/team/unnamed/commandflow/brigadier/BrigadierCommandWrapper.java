package team.unnamed.commandflow.brigadier;

import team.unnamed.commandflow.CommandManager;
import team.unnamed.commandflow.bukkit.BukkitCommandWrapper;
import team.unnamed.commandflow.command.Command;
import team.unnamed.commandflow.translator.Translator;
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
