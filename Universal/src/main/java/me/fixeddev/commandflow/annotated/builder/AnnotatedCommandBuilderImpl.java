package me.fixeddev.commandflow.annotated.builder;

import me.fixeddev.commandflow.annotated.part.PartInjector;

final class AnnotatedCommandBuilderImpl implements AnnotatedCommandBuilder {

    private final PartInjector injector;

    AnnotatedCommandBuilderImpl(PartInjector injector) {
        this.injector = injector;
    }

    @Override
    public CommandDataNode newCommand(String name) {
        return new CommandBuilderNodesImpl(name, injector);
    }

}
