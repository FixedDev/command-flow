package me.fixeddev.commandflow.annotated.part;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface PartInjector {
    /**
     * Gets a {@link PartFactory} for a given {@link Type}.
     *
     * @param type The type of {@link me.fixeddev.commandflow.part.CommandPart} to generate.
     * @return An {@link PartFactory} for a specific {@link Type} of CommandPart.
     */
    @Nullable
    default PartFactory getFactory(Type type) {
        return getFactory(new Key(type));
    }

    /**
     * Gets a {@link PartFactory} for a given {@link Key}.
     *
     * @param key The type of {@link me.fixeddev.commandflow.part.CommandPart} to generate.
     * @return An {@link PartFactory} for a specific {@link Type} of CommandPart.
     */
    @Nullable
    PartFactory getFactory(Key key);

    /**
     * Gets a {@link PartFactory} for a given {@link Key}.
     *
     * @param annotation The type of {@link me.fixeddev.commandflow.part.CommandPart} to generate.
     * @return An {@link PartFactory} for a specific {@link Type} of CommandPart.
     */
    @Nullable
    PartModifier getModifier(Annotation annotation);

    /**
     * Gets a {@link DelegatePartModifier} for a given list of {@link Annotation}.
     *
     * @param annotations The list of {@link Annotation} instances to delegate to.
     * @return An {@link PartFactory} for a specific {@link Type} of CommandPart.
     */
    @NotNull
    default PartModifier getModifiers(List<Annotation> annotations) {
        List<PartModifier> modifiers = new ArrayList<>();

        for (Annotation annotation : annotations) {
            PartModifier modifier = getModifier(annotation);

            if (modifier != null) {
                modifiers.add(modifier);
            }
        }

        return new DelegatePartModifier(modifiers);
    }

    /**
     * Gets a {@link DelegatePartModifier} for a given list of {@link Annotation}.
     *
     * @param annotations The list of {@link Annotation} instances to delegate to.
     * @return An {@link PartFactory} for a specific {@link Type} of CommandPart.
     */
    @NotNull
    default PartModifier getModifiers(Annotation... annotations) {
        return getModifiers(Arrays.asList(annotations));
    }

    void bindModifier(Annotation annotation, PartModifier partModifier);

    default void bindFactory(Type type, PartFactory partFactory) {
        bindFactory(new Key(type), partFactory);
    }

    void bindFactory(Key key, PartFactory factory);

    void install(Module module);

    static PartInjector create() {
        return new SimplePartInjector();
    }
}
