package me.fixeddev.commandflow.annotated.part;

import me.fixeddev.commandflow.part.CommandPart;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * An aggregation of multiple bindings of {@linkplain PartModifier} or {@linkplain PartFactory}, allowing to automatically register
 * a set of modifiers or factories.
 */
public interface Module {

    /**
     * The configure method, where you add the bindings for {@linkplain PartModifier}s or a set of {@linkplain PartFactory}.
     */
    void configure();

    /**
     * This method sets the injector used for the bind methods in this class.
     * <p>
     * You shouldn't use this method directly, let the {@linkplain PartInjector} handle it.
     *
     * @param injector The injector in which this is being installed.
     */
    void setInjector(PartInjector injector);

    /**
     * The {@linkplain PartInjector} used for bindings.
     *
     * @return The {@linkplain PartInjector} that every bind method is being delegated to.
     */
    PartInjector getInjector();

    default void bindModifier(Class<? extends Annotation> annotation, PartModifier partModifier) {
        if (getInjector() == null) {
            throw new IllegalStateException("The bind methods only can be called when the module is installed on an injector!");
        }

        getInjector().bindModifier(annotation, partModifier);
    }

    default void bindFactory(Type type, PartFactory partFactory) {
        if (getInjector() == null) {
            throw new IllegalStateException("The bind methods only can be called when the module is installed on an injector!");
        }

        getInjector().bindFactory(type, partFactory);
    }

    default void bindFactory(Key key, PartFactory factory) {
        if (getInjector() == null) {
            throw new IllegalStateException("The bind methods only can be called when the module is installed on an injector!");
        }

        getInjector().bindFactory(key, factory);
    }

    default void bind(Key key, CommandPart part) {
        if (getInjector() == null) {
            throw new IllegalStateException("The bind methods only can be called when the module is installed on an injector!");
        }

        getInjector().bindPart(key, part);
    }

    default void bind(Type type, CommandPart part) {
        if (getInjector() == null) {
            throw new IllegalStateException("The bind methods only can be called when the module is installed on an injector!");
        }

        getInjector().bindPart(type, part);
    }

}
