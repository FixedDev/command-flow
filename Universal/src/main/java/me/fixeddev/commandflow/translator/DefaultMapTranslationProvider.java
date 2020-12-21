package me.fixeddev.commandflow.translator;

import me.fixeddev.commandflow.Namespace;

import java.util.HashMap;
import java.util.Map;

public class DefaultMapTranslationProvider implements TranslationProvider{

    protected Map<String, String> translations;

    public DefaultMapTranslationProvider() {
        translations = new HashMap<>();
        translations.put("command.subcommand.invalid", "The subcommand %s doesn't exists!");
        translations.put("command.no-permission", "No permission.");
        translations.put("argument.no-more","No more arguments were found, size: %s position: %s");
        translations.put("number.out-range", "The number %s is not within the range min: %s max: %s");
    }

    public String getTranslation(String key) {
        return translations.get(key);
    }

    @Override
    public String getTranslation(Namespace namespace, String key){
        return getTranslation(key);
    }
}
