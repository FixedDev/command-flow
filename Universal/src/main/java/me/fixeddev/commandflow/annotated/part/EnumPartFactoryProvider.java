package me.fixeddev.commandflow.annotated.part;

import me.fixeddev.commandflow.part.defaults.EnumPart;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EnumPartFactoryProvider implements PartFactoryProvider {

    private final Map<Class<?>, PartFactory> cachedEnumPartFactories;

    public EnumPartFactoryProvider() {
        this.cachedEnumPartFactories = new ConcurrentHashMap<>();
    }

    @Override
    public @Nullable PartFactory getFactory(Key key) {
        Type type = key.getType();
        if (type instanceof Class) {
            Class<?> rawType = (Class<?>) type;
            if (rawType.isEnum()) {
                return cachedEnumPartFactories.computeIfAbsent(rawType, k ->
                        (name, modifiers) -> new EnumPart(name, ", ", rawType));
            }
        }
        return null;
    }

}
