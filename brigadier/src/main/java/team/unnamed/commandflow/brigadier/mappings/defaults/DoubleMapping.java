package team.unnamed.commandflow.brigadier.mappings.defaults;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import team.unnamed.commandflow.brigadier.mappings.CommandNodeMapping;
import team.unnamed.commandflow.part.ArgumentPart;

public class DoubleMapping<T> implements CommandNodeMapping<T> {
    @Override
    public CommandNode<T> convert(ArgumentPart part) {
        return RequiredArgumentBuilder.<T, Double>argument(part.getName(), DoubleArgumentType.doubleArg()).build();
    }
}
