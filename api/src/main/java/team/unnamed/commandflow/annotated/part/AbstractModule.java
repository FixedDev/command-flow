package team.unnamed.commandflow.annotated.part;

import team.unnamed.commandflow.part.CommandPart;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * A default implementation of {@linkplain Module}, you just need to implement the configure method.
 */
public abstract class AbstractModule implements Module {

    private PartInjector injector;

    @Override
    public final void configure(PartInjector injector) {
        this.injector = injector;
        configure();
        this.injector = null;
    }

    /**
     * Returns the current {@linkplain PartInjector} used for bindings
     *
     * @return The {@linkplain PartInjector} that every bind method is being delegated to.
     */
    protected final PartInjector getInjector() {
        if (this.injector == null) {
            throw new IllegalStateException("The bind methods only can be called when the module is installed on an injector!");
        }
        return this.injector;
    }

    protected final void bindModifier(Class<? extends Annotation> annotation, PartModifier partModifier) {
        getInjector().bindModifier(annotation, partModifier);
    }

    protected final void bindFactory(Type type, PartFactory partFactory) {
        getInjector().bindFactory(type, partFactory);
    }

    protected final void bindFactory(Key key, PartFactory factory) {
        getInjector().bindFactory(key, factory);
    }

    protected final void bind(Key key, CommandPart part) {
        getInjector().bindPart(key, part);
    }

    protected final void bind(Type type, CommandPart part) {
        getInjector().bindPart(type, part);
    }

    public abstract void configure();

}
