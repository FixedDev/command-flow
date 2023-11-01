package team.unnamed.commandflow.bukkit;

import team.unnamed.commandflow.translator.DefaultMapTranslationProvider;

public class BukkitDefaultTranslationProvider extends DefaultMapTranslationProvider {

    public BukkitDefaultTranslationProvider() {
        translations.put("player.offline", "The player %s is offline!");
        translations.put("sender.unknown", "The sender for the command is unknown!");
        translations.put("sender.only-player", "Only players can execute this command!");
        translations.put("invalid.gamemode", "The gamemode %s is not valid!");
    }

}
