package team.unnamed.commandflow.brigadier;

import team.unnamed.commandflow.bukkit.BukkitDefaultTranslationProvider;

public class BrigadierDefaultTranslationProvider extends BukkitDefaultTranslationProvider {

    public BrigadierDefaultTranslationProvider() {
        translations.put("selector.parse-error", "Failed to parse selector %s:\n %s");
        translations.put("selector.ambiguous", "The selector %s is ambiguous (more than one player matched), leaving it as is.");
    }

}
