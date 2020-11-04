package me.fixeddev.commandflow.bungee;

import net.kyori.text.Component;
import net.kyori.text.serializer.gson.GsonComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class MessageUtils {

    public static BaseComponent[] kyoriToBungee(Component component) {
        GsonComponentSerializer componentSerializer = GsonComponentSerializer.INSTANCE;

        String serializedComponent = componentSerializer.serialize(component);

        return ComponentSerializer.parse(serializedComponent);
    }
}
