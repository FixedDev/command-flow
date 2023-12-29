package team.unnamed.commandflow.bukkit.part;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.bukkit.BukkitCommonConstants;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.exception.CommandException;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.stack.ArgumentStack;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        CommandSender sender = context.getObject(CommandSender.class, BukkitCommonConstants.SENDER_NAMESPACE);

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
