package me.fixeddev.commandflow;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NamespaceImpl implements Namespace {
    private final Map<Class<?>, Map<String, Object>> backing;

    public NamespaceImpl() {
        backing = new ConcurrentHashMap<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getObject(Class<T> clazz, String name) {
        return (T) backing.getOrDefault(clazz, new ConcurrentHashMap<>()).get(name);
    }

    @Override
    public <T> void setObject(Class<T> clazz, String name, T object) {
        Map<String, Object> map = backing.computeIfAbsent(clazz, key -> new ConcurrentHashMap<>());

        map.put(name, object);
    }
}