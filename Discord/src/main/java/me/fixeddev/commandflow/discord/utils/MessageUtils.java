package me.fixeddev.commandflow.discord.utils;

import net.kyori.text.Component;
import net.kyori.text.serializer.plain.PlainComponentSerializer;

public class MessageUtils {

    public static String componentToString(Component component) {
        PlainComponentSerializer serializer = PlainComponentSerializer.INSTANCE;

        return serializer.serialize(component);
    }
}
