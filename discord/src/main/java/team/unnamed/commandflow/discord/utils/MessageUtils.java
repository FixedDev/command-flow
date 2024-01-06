package team.unnamed.commandflow.discord.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class MessageUtils {

    public static String componentToString(Component component) {
        PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();

        return serializer.serialize(component);
    }
}
