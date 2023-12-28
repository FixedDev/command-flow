package team.unnamed.commandflow.bungee;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class MessageUtils {

    public static BaseComponent[] kyoriToBungee(Component component) {
        GsonComponentSerializer componentSerializer = GsonComponentSerializer.gson();

        String serializedComponent = componentSerializer.serialize(component);

        return ComponentSerializer.parse(serializedComponent);
    }

}
