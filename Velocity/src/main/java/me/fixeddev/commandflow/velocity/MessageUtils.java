package me.fixeddev.commandflow.velocity;

import net.kyori.adventure.text.Component;

public class MessageUtils {

    public static Component kyoriToVelocityKyori(net.kyori.text.Component component) {
        net.kyori.text.serializer.gson.GsonComponentSerializer componentSerializer = net.kyori.text.serializer.gson.GsonComponentSerializer.INSTANCE;

        String serializedComponent = componentSerializer.serialize(component);

        return net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson().deserialize(serializedComponent);
    }
}
