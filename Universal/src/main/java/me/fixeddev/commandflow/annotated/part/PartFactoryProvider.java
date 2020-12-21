package me.fixeddev.commandflow.annotated.part;

import org.jetbrains.annotations.Nullable;

/**
 * Represents a factory of {@link PartFactory} that finds
 * the correct {@link PartFactory} for the specified {@link Key},
 * this allows the user to create variable {@link PartFactory}s
 * using the given {@link Key}
 *
 * <p>This is used by the {@link EnumPartFactoryProvider} to
 * instantiate an enum part factory when the given key wraps an
 * enum type (checked with {@link Class#isEnum()})</p>
 */
public interface PartFactoryProvider {

    /**
     * Finds a factory for the given {@code key}.
     *
     * @param key The factory key
     * @return The found factory, null if not found. If a null
     * reference is returned, the {@link PartInjector} must check
     * for the next {@linkplain PartFactoryProvider}
     */
    @Nullable
    PartFactory getFactory(Key key);

}
