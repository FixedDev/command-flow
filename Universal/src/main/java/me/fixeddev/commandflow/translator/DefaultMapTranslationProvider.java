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
        translations.put("invalid.byte", "The number %s is not a valid byte!");
        translations.put("invalid.integer", "The number %s is not a valid integer!");
        translations.put("invalid.float", "The number %s is not a valid float!");
        translations.put("invalid.double", "The number %s is not a valid double!");
        translations.put("invalid.boolean", "The string %s is not a valid boolean!");
    }

    public String getTranslation(String key) {
        return translations.get(key);
    }

    @Override
    public String getTranslation(Namespace namespace, String key){
        return getTranslation(key);
    }
}
