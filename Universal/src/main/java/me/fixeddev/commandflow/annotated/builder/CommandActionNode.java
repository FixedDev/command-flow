package me.fixeddev.commandflow.annotated.builder;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.command.Action;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

public interface CommandActionNode extends Buildable {
    @NotNull SubCommandsNode action(@NotNull Method method,
                                    @NotNull CommandClass commandClass);

    @NotNull SubCommandsNode action(@NotNull Action action);
}
