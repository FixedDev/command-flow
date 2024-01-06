package team.unnamed.commandflow.bukkit.sender;

import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;

public class DefaultMessageSender implements MessageSender {
    @Override
    public void sendMessage(CommandSender sender, Component component) {
        BaseComponent[] components = MessageUtils.kyoriToBungee(component);

        MessageUtils.sendMessage(sender, components);
    }
}
