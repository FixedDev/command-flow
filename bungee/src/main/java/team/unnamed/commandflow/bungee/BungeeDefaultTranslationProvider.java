package team.unnamed.commandflow.bungee;

import team.unnamed.commandflow.translator.DefaultMapTranslationProvider;

public class BungeeDefaultTranslationProvider extends DefaultMapTranslationProvider {

    public BungeeDefaultTranslationProvider() {
        translations.put("player.offline", "The player %s is offline!");
        translations.put("sender.unknown", "The sender for the command is unknown!");
        translations.put("sender.only-player", "Only players can execute this command!");
    }

}
