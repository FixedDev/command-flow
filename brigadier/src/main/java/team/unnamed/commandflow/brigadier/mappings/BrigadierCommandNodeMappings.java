package team.unnamed.commandflow.brigadier.mappings;

import java.lang.reflect.Type;
import java.util.Optional;

public interface BrigadierCommandNodeMappings<T> {
    default Optional<CommandNodeMapping<T>> getMapping(Class<?> type) {
        return getMapping(type);
    }

    Optional<CommandNodeMapping<T>> getMapping(Type type);

    Builder<T> toBuilder();

    static <T> Builder<T> builder() {
        return new BrigadierCommandNodeMappingsImpl.Builder<>();
    }

    interface Builder<T> {
        default Builder<T> addMapping(Class<?> type, CommandNodeMapping<T> mapping) {
            return addMapping(type, mapping);
        }

        Builder<T> addMapping(Type type, CommandNodeMapping<T> mapping);

        BrigadierCommandNodeMappings<T> build();
    }
}
