package me.fixeddev.commandflow.bukkit;

import me.fixeddev.commandflow.translator.DefaultMapTranslationProvider;

public class BukkitDefaultTranslationProvider extends DefaultMapTranslationProvider {
    public BukkitDefaultTranslationProvider() {
        translations.put("player.offline", "The player %s is offline!");
        translations.put("sender.unknown", "The sender for the command is unknown!");
        translations.put("sender.only-player", "Only players can execute this command!");
    }
}
