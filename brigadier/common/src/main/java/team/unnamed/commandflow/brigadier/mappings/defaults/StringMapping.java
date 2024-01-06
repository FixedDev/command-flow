package team.unnamed.commandflow.brigadier.mappings.defaults;

import team.unnamed.commandflow.brigadier.mappings.CommandNodeMapping;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import team.unnamed.commandflow.part.ArgumentPart;
import team.unnamed.commandflow.part.defaults.StringPart;

public class StringMapping<T> implements CommandNodeMapping<T> {
    @Override
    public CommandNode<T> convert(ArgumentPart part) {
        StringPart stringPart = (StringPart) part;

        if (stringPart.isConsumeAll()) {
            return RequiredArgumentBuilder.<T, String>argument(part.getName(), StringArgumentType.greedyString()).build();
        } else {
            return RequiredArgumentBuilder.<T, String>argument(part.getName(), StringArgumentType.word()).build();
        }
    }
}
