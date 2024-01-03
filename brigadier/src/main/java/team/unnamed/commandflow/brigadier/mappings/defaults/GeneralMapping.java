package team.unnamed.commandflow.brigadier.mappings.defaults;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import org.bukkit.command.CommandSender;
import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.Namespace;
import team.unnamed.commandflow.SimpleCommandContext;
import team.unnamed.commandflow.brigadier.mappings.CommandNodeMapping;
import team.unnamed.commandflow.part.ArgumentPart;
import team.unnamed.commandflow.stack.SimpleArgumentStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GeneralMapping<T> implements CommandNodeMapping<T> {
    @Override
    public CommandNode<T> convert(ArgumentPart part) {
        return RequiredArgumentBuilder.<T, String>argument(part.getName(), StringArgumentType.word())
                .suggests((context, builder) -> {
                    T sender = context.getSource();

                    Namespace namespace = Namespace.create();
                    //noinspection unchecked
                    namespace.setObject((Class<T>) sender.getClass(), "sender", sender);

                    CommandContext commandContext = new SimpleCommandContext(namespace, new ArrayList<>());

                    List<String> suggestions = part.getSuggestions(commandContext, new SimpleArgumentStack(Collections.singletonList("")));

                    if (suggestions != null) {
                        for (String suggestion : suggestions) {
                            builder.suggest(suggestion);
                        }
                    }

                    return builder.buildFuture();
                }).build();
    }
}
