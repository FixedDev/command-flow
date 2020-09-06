package me.fixeddev.commandflow.annotated;

import me.fixeddev.commandflow.annotated.builder.AnnotatedCommandBuilder;
import me.fixeddev.commandflow.annotated.part.PartInjector;
import me.fixeddev.commandflow.command.Command;

import java.util.List;

public interface AnnotatedCommandTreeBuilder {
    List<Command> fromClass(CommandClass commandClass);

    static AnnotatedCommandTreeBuilder create(AnnotatedCommandBuilder builder, SubCommandInstanceCreator instanceCreator) {
        return new AnnotatedCommandTreeBuilderImpl(builder, instanceCreator);
    }

    static AnnotatedCommandTreeBuilder create(PartInjector injector) {
        return new AnnotatedCommandTreeBuilderImpl(injector);
    }
}




