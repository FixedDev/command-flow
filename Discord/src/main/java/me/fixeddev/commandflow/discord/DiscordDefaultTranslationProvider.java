package me.fixeddev.commandflow.discord;

import me.fixeddev.commandflow.translator.DefaultMapTranslationProvider;

public class DiscordDefaultTranslationProvider extends DefaultMapTranslationProvider {

    public DiscordDefaultTranslationProvider() {
        translations.put("unknown.member", "The member is unknown!");
        translations.put("unknown.channel", "The channel is unknown!");
        translations.put("unknown.user", "The user is unknown!");
    }
}
