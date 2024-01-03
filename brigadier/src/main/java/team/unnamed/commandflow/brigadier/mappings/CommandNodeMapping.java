package team.unnamed.commandflow.brigadier.mappings;

import com.mojang.brigadier.tree.CommandNode;
import team.unnamed.commandflow.part.ArgumentPart;

public interface CommandNodeMapping<T> {
    CommandNode<T> convert(ArgumentPart argumentPart);
}
