package me.fixeddev.commandflow.bukkit;

import net.kyori.text.Component;
import net.kyori.text.serializer.gson.GsonComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageUtils {

    public static void sendMessage(CommandSender sender, BaseComponent[] components) {
        if (sender instanceof Player) {
            ((Player) sender).spigot().sendMessage(components);
        } else {
            StringBuilder builder = new StringBuilder();

            for (BaseComponent component : components) {
                builder.append(component.toLegacyText());
            }

            sender.sendMessage(builder.toString());
        }
    }

    public static BaseComponent[] kyoriToBungee(Component component) {
        GsonComponentSerializer componentSerializer = GsonComponentSerializer.INSTANCE;

        String serializedComponent = componentSerializer.serialize(component);
        BaseComponent[] components = ComponentSerializer.parse(serializedComponent);

        return components;
    }

}
