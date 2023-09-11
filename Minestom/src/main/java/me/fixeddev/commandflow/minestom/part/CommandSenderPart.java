package me.fixeddev.commandflow.minestom.part;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.minestom.MinestomCommandManager;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.Nullable;

public class CommandSenderPart implements CommandPart {

    private final String name;
    private boolean expectPlayer;

    public CommandSenderPart(String name, boolean expectPlayer) {
        this.name = name;
        this.expectPlayer = expectPlayer;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack, @Nullable CommandPart caller) throws ArgumentParseException {
        CommandSender sender = context.getObject(CommandSender.class, MinestomCommandManager.SENDER_NAMESPACE);

        if (sender == null) {
            throw new ArgumentParseException(Component.translatable("sender.unknown"))
                    .setArgument(this);
        }

        if (expectPlayer && !(sender instanceof Player)) {
            throw new ArgumentParseException(Component.translatable("sender.only-player"))
                    .setArgument(this);
        }

        context.setValue(this, sender);
    }
}