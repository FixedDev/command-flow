package team.unnamed.commandflow.bukkit.sender;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

public interface MessageSender {
    void sendMessage(CommandSender sender, Component component);
}
