package me.fixeddev.commandflow.annotated.builder;

import me.fixeddev.commandflow.annotated.part.PartInjector;

public interface AnnotatedCommandBuilder {
    CommandDataNode newCommand(String name);

    static AnnotatedCommandBuilder create(PartInjector injector) {
        return new AnnotatedCommandBuilderImpl(injector);
    }
}
