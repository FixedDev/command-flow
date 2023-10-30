package me.fixeddev.commandflow.velocity.part;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.exception.CommandException;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import me.fixeddev.commandflow.velocity.VelocityCommandManager;
import net.kyori.adventure.text.Component;

import java.util.Objects;

public class PlayerSenderPart implements CommandPart {

    private final String name;

    public PlayerSenderPart(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack, CommandPart parent) throws ArgumentParseException {
        CommandSource sender = context.getObject(CommandSource.class, VelocityCommandManager.SENDER_NAMESPACE);

        if (sender != null) {
            if (sender instanceof Player) {
                context.setValue(this, sender);

                return;
            }

            throw new ArgumentParseException(Component.translatable("sender.only-player"));
        }

        throw new CommandException(Component.translatable("sender.unknown"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerSenderPart)) return false;
        PlayerSenderPart that = (PlayerSenderPart) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
