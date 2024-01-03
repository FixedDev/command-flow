package team.unnamed.commandflow.brigadier.mappings;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

final class BrigadierCommandNodeMappingsImpl<T> implements BrigadierCommandNodeMappings<T> {

    private final Map<Type, CommandNodeMapping<T>> mappingMap;

    BrigadierCommandNodeMappingsImpl(Map<Type, CommandNodeMapping<T>> mappingMap) {
        this.mappingMap = mappingMap;
    }

    @Override
    public Optional<CommandNodeMapping<T>> getMapping(Type type) {
        return Optional.ofNullable(mappingMap.get(type));
    }

    @Override
    public BrigadierCommandNodeMappings.Builder<T> toBuilder() {
        return new Builder<>(mappingMap);
    }

    final static class Builder<T> implements BrigadierCommandNodeMappings.Builder<T> {

        private final Map<Type, CommandNodeMapping<T>> mappingMap;

        Builder() {
            mappingMap = new HashMap<>();
        }

        Builder(Map<Type, CommandNodeMapping<T>> mappingMap) {
            this.mappingMap = new HashMap<>(mappingMap);
        }

        @Override
        public BrigadierCommandNodeMappings.Builder<T> addMapping(Type type, CommandNodeMapping<T> mapping) {
            mappingMap.put(type, mapping);
            return this;
        }

        @Override
        public BrigadierCommandNodeMappings<T> build() {
            return new BrigadierCommandNodeMappingsImpl<>(mappingMap);
        }
    }
}
