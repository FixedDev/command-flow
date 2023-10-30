package team.unnamed.commandflow.bukkit;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
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
        GsonComponentSerializer componentSerializer = GsonComponentSerializer.gson();

        String serializedComponent = componentSerializer.serialize(component);

        return ComponentSerializer.parse(serializedComponent);
    }

}
