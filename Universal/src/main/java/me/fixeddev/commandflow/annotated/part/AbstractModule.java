package me.fixeddev.commandflow.annotated.part;

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
