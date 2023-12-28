package team.unnamed.commandflow.bukkit.part;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.bukkit.BukkitCommandManager;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.exception.CommandException;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.stack.ArgumentStack;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

import java.util.Objects;

public class CommandSenderPart implements CommandPart {

    private final String name;

    public CommandSenderPart(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack, CommandPart parent) throws ArgumentParseException {
        CommandSender sender = context.getObject(CommandSender.class, BukkitCommandManager.SENDER_NAMESPACE);

        if (sender != null) {
            context.setValue(this, sender);

            return;
        }

        throw new CommandException(Component.translatable("sender.unknown"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommandSenderPart)) return false;
        CommandSenderPart that = (CommandSenderPart) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
