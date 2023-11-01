package team.unnamed.commandflow.discord;

import team.unnamed.commandflow.translator.DefaultMapTranslationProvider;

public class DiscordDefaultTranslationProvider extends DefaultMapTranslationProvider {

    public DiscordDefaultTranslationProvider() {
        translations.put("unknown.member", "The member is unknown!");
        translations.put("unknown.channel", "The channel is unknown!");
        translations.put("unknown.user", "The user is unknown!");
    }
}
