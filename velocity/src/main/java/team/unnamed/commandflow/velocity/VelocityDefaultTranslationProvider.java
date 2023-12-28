package team.unnamed.commandflow.velocity;

import team.unnamed.commandflow.translator.DefaultMapTranslationProvider;

public class VelocityDefaultTranslationProvider extends DefaultMapTranslationProvider {

    public VelocityDefaultTranslationProvider() {
        translations.put("player.offline", "The player %s is offline!");
        translations.put("sender.unknown", "The sender for the command is unknown!");
        translations.put("sender.only-player", "Only players can execute this command!");
    }

}
