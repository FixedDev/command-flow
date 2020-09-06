package me.fixeddev.commandflow.annotated.builder;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.part.CommandPart;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

public interface CommandPartsNode extends Buildable {
    @NotNull CommandActionNode ofMethodParameters(@NotNull Method method,
                                                  @NotNull CommandClass handler);

    @NotNull CommandPartsNode addPart(@NotNull CommandPart part);

    @NotNull CommandActionNode action();
}
