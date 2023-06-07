package me.fixeddev.commandflow.minestom;

import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.NamespaceImpl;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.exception.ArgumentSyntaxException;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FakeArgument extends Argument<Object> {

    public FakeArgument(CommandManager commandManager) {
        super("fake");

        setSuggestionCallback((sender, context, suggestion) -> {
            Namespace namespace = new NamespaceImpl();
            namespace.setObject(CommandSender.class, MinestomCommandManager.SENDER_NAMESPACE, sender);

            List<String> suggestions = commandManager.getSuggestions(namespace, suggestion.getInput());

            suggestions.forEach(s -> suggestion.addEntry(new SuggestionEntry(s)));
        });
    }

    @Override
    public @NotNull Object parse(@NotNull String input) throws ArgumentSyntaxException {
        return null;
    }

    @Override
    public String parser() {
        return null;
    }

}