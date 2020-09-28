package me.fixeddev.commandflow.annotated.builder;

import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.part.defaults.SubCommandPart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;

public interface SubCommandsNode extends Buildable {
    SubCommandsNode addSubCommand(@NotNull Command command);

    SubCommandsNode addSubCommand(@NotNull CommandDataNode commandDataNode);

    SubCommandsNode setSubCommandHandler(@Nullable SubCommandPart.SubCommandHandler subCommandHandler);

    SubCommandsNode setModifiers(Annotation... modifiers);

    SubCommandsNode optional();
}
