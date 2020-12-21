package me.fixeddev.commandflow.annotated.part;

import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimplePartInjector implements PartInjector {

    private Map<Key, PartFactory> factoryBindings;
    private Map<Class<? extends Annotation>, PartModifier> modifiers;

    public SimplePartInjector() {
        this.factoryBindings = new ConcurrentHashMap<>();
        this.modifiers = new ConcurrentHashMap<>();
    }

    @Override
    public @Nullable PartFactory getFactory(Key key) {
        return factoryBindings.get(key);
    }

    @Override
    public @Nullable PartModifier getModifier(Class<? extends Annotation> annotation) {
        return modifiers.get(annotation);
    }

    @Override
    public void bindModifier(Class<? extends Annotation> annotation, PartModifier partModifier) {
        if (modifiers.putIfAbsent(annotation, partModifier) != null) {
            throw new IllegalArgumentException("A modifier with the key " + annotation.toString() + " is already present!");
        }
    }

    @Override
    public void bindFactory(Key key, PartFactory factory) {
        if (factoryBindings.putIfAbsent(key, factory) != null) {
            throw new IllegalArgumentException("A factory with the key " + key.toString() + " is already present!");
        }
    }

    @Override
    public void install(Module module) {
        module.setInjector(this);
        module.configure();
        module.setInjector(null);
    }
}
