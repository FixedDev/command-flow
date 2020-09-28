package me.fixeddev.commandflow.bukkit.part;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.exception.CommandException;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import net.kyori.text.TranslatableComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSenderPart implements CommandPart {
    private String name;

    public CommandSenderPart(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack) throws ArgumentParseException {
        CommandSender sender = context.getObject(CommandSender.class, BukkitCommandManager.SENDER_NAMESPACE);

        if (sender instanceof Player) {
            context.setValue(this, sender);

            return;
        }

        throw new CommandException(TranslatableComponent.of("sender.unknown"));
    }
}
