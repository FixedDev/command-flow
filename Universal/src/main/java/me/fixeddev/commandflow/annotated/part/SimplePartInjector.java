package me.fixeddev.commandflow.annotated.part;

import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimplePartInjector implements PartInjector {

    private final Deque<PartFactoryProvider> providers;
    private final DirectPartFactoryProvider directProvider;
    private Map<Class<? extends Annotation>, PartModifier> modifiers;

    public SimplePartInjector() {
        this.providers = new LinkedList<>();
        this.directProvider = new DirectPartFactoryProvider();
        this.modifiers = new ConcurrentHashMap<>();

        this.providers.add(directProvider);
        this.providers.add(new EnumPartFactoryProvider());
    }

    @Override
    public @Nullable PartFactory getFactory(Key key) {
        for (PartFactoryProvider provider : providers) {
            PartFactory factory = provider.getFactory(key);
            if (factory != null) {
                return factory;
            }
        }
        return null;
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
        directProvider.bindFactory(key, factory);
    }

    @Override
    public void addProviderToHead(PartFactoryProvider factoryProvider) {
        providers.addFirst(factoryProvider);
    }

    @Override
    public void addProviderToTail(PartFactoryProvider factoryProvider) {
        providers.addLast(factoryProvider);
    }

    @Override
    public void install(Module module) {
        module.setInjector(this);
        module.configure();
        module.setInjector(null);
    }
}
