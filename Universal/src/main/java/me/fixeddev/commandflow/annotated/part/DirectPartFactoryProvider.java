package me.fixeddev.commandflow.annotated.part;

import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DirectPartFactoryProvider implements PartFactoryProvider {

    private final Map<Key, PartFactory> factoryBindings;

    public DirectPartFactoryProvider() {
        this.factoryBindings = new ConcurrentHashMap<>();
    }

    @Override
    public @Nullable PartFactory getFactory(Key key) {
        return null;
    }

    public void bindFactory(Key key, PartFactory factory) {
        if (factoryBindings.putIfAbsent(key, factory) != null) {
            throw new IllegalArgumentException("A factory with the key " + key + " is already present!");
        }
    }

}
