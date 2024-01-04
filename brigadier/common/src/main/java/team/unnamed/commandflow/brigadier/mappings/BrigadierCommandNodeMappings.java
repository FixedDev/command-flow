package team.unnamed.commandflow.brigadier.mappings;

import team.unnamed.commandflow.brigadier.mappings.defaults.BooleanMapping;
import team.unnamed.commandflow.brigadier.mappings.defaults.DoubleMapping;
import team.unnamed.commandflow.brigadier.mappings.defaults.IntegerMapping;
import team.unnamed.commandflow.brigadier.mappings.defaults.LongMapping;
import team.unnamed.commandflow.brigadier.mappings.defaults.StringMapping;
import team.unnamed.commandflow.part.defaults.BooleanPart;
import team.unnamed.commandflow.part.defaults.DoublePart;
import team.unnamed.commandflow.part.defaults.IntegerPart;
import team.unnamed.commandflow.part.defaults.LongPart;
import team.unnamed.commandflow.part.defaults.StringPart;

import java.lang.reflect.Type;
import java.util.Optional;

public interface BrigadierCommandNodeMappings<T> {

    /**
     * Obtains the {@link CommandNodeMapping} for the specified type.
     *
     * @param type The type to get a conversion mapping for.
     * @return A {@link CommandNodeMapping}, if it exists, suitable for converting a {@linkplain team.unnamed.commandflow.part.ArgumentPart} of the specified type.
     */
    default Optional<CommandNodeMapping<T>> getMappingByClass(Class<?> type) {
        return getMapping(type);
    }

    /**
     * Obtains the {@link CommandNodeMapping} for the specified type.
     *
     * @param type The type to get a conversion mapping for.
     * @return A {@link CommandNodeMapping}, if it exists, suitable for converting a {@linkplain team.unnamed.commandflow.part.ArgumentPart} of the specified type.
     */
    Optional<CommandNodeMapping<T>> getMapping(Type type);

    /**
     * Converts this Mappings instance into a builder, allowing for more mappings to be added.
     *
     * @return A new builder for a {@linkplain BrigadierCommandNodeMappings} instance, with the mappings of this instance already added.
     */
    Builder<T> toBuilder();

    static <T> BrigadierCommandNodeMappings.Builder<T> defaultMappings() {
        return BrigadierCommandNodeMappings.<T>builder()
                .addMappingByClass(BooleanPart.class, new BooleanMapping<>())
                .addMappingByClass(DoublePart.class, new DoubleMapping<>())
                .addMappingByClass(IntegerPart.class, new IntegerMapping<>())
                .addMappingByClass(LongPart.class, new LongMapping<>())
                .addMappingByClass(StringPart.class, new StringMapping<>());
    }


    static <T> Builder<T> builder() {
        return new BrigadierCommandNodeMappingsImpl.Builder<>();
    }

    interface Builder<T> {
        default Builder<T> addMappingByClass(Class<?> type, CommandNodeMapping<T> mapping) {
            return addMapping(type, mapping);
        }

        Builder<T> addMapping(Type type, CommandNodeMapping<T> mapping);

        BrigadierCommandNodeMappings<T> build();
    }
}
