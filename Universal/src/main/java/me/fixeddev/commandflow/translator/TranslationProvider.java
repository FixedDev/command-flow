package me.fixeddev.commandflow.translator;

import me.fixeddev.commandflow.Namespace;

public interface TranslationProvider {

    String getTranslation(Namespace namespace, String key);

}
