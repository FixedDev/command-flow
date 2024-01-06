package team.unnamed.commandflow.brigadier.mappings.defaults;

import team.unnamed.commandflow.brigadier.mappings.CommandNodeMapping;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.Namespace;
import team.unnamed.commandflow.SimpleCommandContext;
import team.unnamed.commandflow.part.ArgumentPart;
import team.unnamed.commandflow.stack.SimpleArgumentStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class GeneralMapping<T, V> implements CommandNodeMapping<T> {
    private final Function<T, V> senderMapping;

    public GeneralMapping(Function<T, V> senderMapping) {
        this.senderMapping = senderMapping;
    }

    @Override
    public CommandNode<T> convert(ArgumentPart part) {
        return RequiredArgumentBuilder.<T, String>argument(part.getName(), StringArgumentType.word())
                .suggests((context, builder) -> {
                    T sender = context.getSource();
                    V platformSender = senderMapping.apply(sender);

                    Namespace namespace = Namespace.create();
                    //noinspection unchecked
                    namespace.setObject((Class<V>) platformSender.getClass(), "sender", platformSender);

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
