package team.unnamed.commandflow.annotated.builder;

import team.unnamed.commandflow.command.Command;
import team.unnamed.commandflow.part.defaults.SubCommandPart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;

// TODO: Add documentation for this class.
public interface SubCommandsNode extends Buildable {

    SubCommandsNode addSubCommand(@NotNull Command command);

    SubCommandsNode addSubCommand(@NotNull CommandDataNode commandDataNode);

    SubCommandsNode setSubCommandHandler(@Nullable SubCommandPart.SubCommandHandler subCommandHandler);

    SubCommandsNode setModifiers(Annotation... modifiers);

    default SubCommandsNode argumentsOrSubCommand() {
        return argumentsOrSubCommand(false);
    }

    SubCommandsNode argumentsOrSubCommand(boolean reversed);

    SubCommandsNode optional();

}
