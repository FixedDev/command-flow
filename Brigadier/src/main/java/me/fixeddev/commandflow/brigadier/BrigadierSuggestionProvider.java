package me.fixeddev.commandflow.brigadier;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.SimpleCommandContext;
import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.stack.SimpleArgumentStack;
import me.lucko.commodore.Commodore;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class BrigadierSuggestionProvider implements SuggestionProvider<Object> {

    private final Commodore commodore;
    private final ArgumentPart partToComplete;

    public BrigadierSuggestionProvider(Commodore commodore, ArgumentPart partToComplete) {
        this.commodore = commodore;
        this.partToComplete = partToComplete;
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<Object> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        CommandSender sender = commodore.getBukkitSender(context.getSource());

        Namespace namespace = Namespace.create();
        namespace.setObject(CommandSender.class, BukkitCommandManager.SENDER_NAMESPACE, sender);

        me.fixeddev.commandflow.CommandContext commandContext = new SimpleCommandContext(namespace, new ArrayList<>());

        for (String suggestion : partToComplete.getSuggestions(commandContext, new SimpleArgumentStack(new ArrayList<>()))) {
            builder.suggest(suggestion);
        }

        return builder.buildFuture();
    }
}
