package me.fixeddev.commandflow.annotated.builder;

import me.fixeddev.commandflow.annotated.part.PartInjector;

public class AnnotatedCommandBuilderImpl implements AnnotatedCommandBuilder {
    private PartInjector injector;

    public AnnotatedCommandBuilderImpl(PartInjector injector) {
        this.injector = injector;
    }

    @Override
    public CommandDataNode newCommand(String name) {
        return new CommandBuilderNodesImpl(name, injector);
    }
}
