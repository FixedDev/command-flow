package me.fixeddev.commandflow.annotated;

/**
 * An interface with the purpose of creating a {@link CommandClass} instance that is a sub commands class of another
 * {@link CommandClass}, with that class being the parent of this one.
 */
public interface SubCommandInstanceCreator {

    /**
     * Create an instance of a specified {@link CommandClass} with a given {@link CommandClass} as parent.
     *
     * @param clazz  The type of the {@link CommandClass} to create an instance of.
     * @param parent The parent instance of this {@link CommandClass}.
     * @return A {@link CommandClass} instance.
     */
    CommandClass createInstance(Class<? extends CommandClass> clazz, CommandClass parent);

}
