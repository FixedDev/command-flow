package me.fixeddev.commandflow.brigadier;

import me.fixeddev.commandflow.bukkit.BukkitDefaultTranslationProvider;

public class BrigadierDefaultTranslationProvider extends BukkitDefaultTranslationProvider {
    public BrigadierDefaultTranslationProvider() {
        translations.put("selector.parse-error", "Failed to parse selector: %s for %s");
        translations.put("selector.ambiguous", "The selector %s is ambiguous (more than one player matched), leaving it as is.");
    }
}
