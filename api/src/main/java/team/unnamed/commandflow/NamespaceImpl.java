package team.unnamed.commandflow;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

final class NamespaceImpl implements Namespace {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NamespaceImpl)) return false;
        NamespaceImpl namespace = (NamespaceImpl) o;
        return Objects.equals(backing, namespace.backing);
    }

    @Override
    public int hashCode() {
        return Objects.hash(backing);
    }

}