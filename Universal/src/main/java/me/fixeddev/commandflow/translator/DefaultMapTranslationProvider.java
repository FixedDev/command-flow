package me.fixeddev.commandflow.translator;

import java.util.HashMap;
import java.util.Map;

public class DefaultMapTranslationProvider implements TranslationProvider{

    private Map<String, String> translations;

    public DefaultMapTranslationProvider() {
        translations = new HashMap<>();
        translations.put("invalid.subcommand", "The subcommand %s doesn't exists!");
    }

    @Override
    public String getTranslation(String key) {
        return translations.get(key);
    }
}
