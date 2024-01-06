package team.unnamed.commandflow.bukkit.paper;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import team.unnamed.commandflow.bukkit.sender.MessageSender;

public class PaperMessageSender implements MessageSender {
    @Override
    public void sendMessage(CommandSender sender, Component component) {
        sender.sendMessage(component);
    }
}
