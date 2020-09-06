package me.fixeddev.commandflow.annotated.builder;

import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.part.SubCommandPart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SubCommandsNode extends Buildable {
    SubCommandsNode addSubCommand(@NotNull Command command);

    SubCommandsNode addSubCommand(@NotNull CommandDataNode commandDataNode);

    SubCommandsNode setSubCommandHandler(@Nullable SubCommandPart.SubCommandHandler subCommandHandler);
}
