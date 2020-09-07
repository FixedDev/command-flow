package me.fixeddev.commandflow.annotated.part;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public interface Module {
    void configure();

    void setInjector(PartInjector injector);

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
}
