package team.unnamed.commandflow.translator;

import team.unnamed.commandflow.Namespace;

public interface TranslationProvider {

    String getTranslation(Namespace namespace, String key);

}
