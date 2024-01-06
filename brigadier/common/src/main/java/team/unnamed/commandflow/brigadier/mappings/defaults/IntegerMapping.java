package team.unnamed.commandflow.brigadier.mappings.defaults;

import team.unnamed.commandflow.brigadier.mappings.CommandNodeMapping;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import team.unnamed.commandflow.part.ArgumentPart;

public class IntegerMapping<T> implements CommandNodeMapping<T> {
    @Override
    public CommandNode<T> convert(ArgumentPart part) {
        return RequiredArgumentBuilder.<T, Integer>argument(part.getName(), IntegerArgumentType.integer()).build();
    }
}
