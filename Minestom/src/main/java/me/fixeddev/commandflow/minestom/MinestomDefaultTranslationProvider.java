package me.fixeddev.commandflow.minestom;

import me.fixeddev.commandflow.translator.DefaultMapTranslationProvider;

public class MinestomDefaultTranslationProvider extends DefaultMapTranslationProvider {

    public MinestomDefaultTranslationProvider() {
        translations.put("player.offline", "The player %s is offline!");
        translations.put("sender.unknown", "The sender for the command is unknown!");
        translations.put("sender.only-player", "Only players can execute this command!");
        translations.put("invalid.gamemode", "The gamemode %s is not valid!");
    }
}
