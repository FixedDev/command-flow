package me.fixeddev.commandflow.translator;

import java.util.HashMap;
import java.util.Map;

public class DefaultMapTranslationProvider implements TranslationProvider{

    private Map<String, String> translations;

    public DefaultMapTranslationProvider() {
        translations = new HashMap<>();
        translations.put("command.subcommand.invalid", "The subcommand %s doesn't exists!");
        translations.put("command.no-permission", "No permission.");
        translations.put("argument.no-more","No more arguments were found, size: %s position: %s");
    }

    @Override
    public String getTranslation(String key) {
        return translations.get(key);
    }
}
