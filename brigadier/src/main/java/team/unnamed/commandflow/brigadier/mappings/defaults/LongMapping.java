package team.unnamed.commandflow.brigadier.mappings.defaults;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import team.unnamed.commandflow.brigadier.mappings.CommandNodeMapping;
import team.unnamed.commandflow.part.ArgumentPart;

public class LongMapping<T> implements CommandNodeMapping<T> {
    @Override
    public CommandNode<T> convert(ArgumentPart part) {
        return RequiredArgumentBuilder.<T, Long>argument(part.getName(), LongArgumentType.longArg()).build();
    }
}
