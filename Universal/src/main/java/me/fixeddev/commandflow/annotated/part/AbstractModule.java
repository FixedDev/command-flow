package me.fixeddev.commandflow.annotated.part;

/**
 * A default implementation of {@linkplain Module}, you just need to implement the configure method.
 */
public abstract class AbstractModule implements Module {
    private PartInjector injector;

    @Override
    public void setInjector(PartInjector injector) {
        this.injector = injector;
    }

    @Override
    public PartInjector getInjector() {
        return injector;
    }
}
