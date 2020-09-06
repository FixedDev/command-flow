package me.fixeddev.commandflow.annotated.builder;

import me.fixeddev.commandflow.command.Command;
import net.kyori.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public interface CommandDataNode extends Buildable {
    @NotNull CommandDataNode aliases(@NotNull List<String> aliases);

    @NotNull default CommandDataNode aliases(@NotNull String... aliases) {
        return aliases(Arrays.asList(aliases));
    }

    @NotNull CommandDataNode addAlias(@NotNull String alias);

    @NotNull CommandDataNode description(@NotNull Component component);

    @NotNull CommandDataNode permission(@NotNull String permission);

    @NotNull CommandDataNode permissionMessage(@NotNull Component permissionMessage);

    @NotNull CommandPartsNode parts();
}
