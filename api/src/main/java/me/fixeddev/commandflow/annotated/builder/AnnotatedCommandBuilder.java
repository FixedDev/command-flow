package me.fixeddev.commandflow.annotated.builder;

import me.fixeddev.commandflow.annotated.part.PartInjector;

public interface AnnotatedCommandBuilder {

    /**
     * Creates a new builder to create a {@link me.fixeddev.commandflow.command.Command} instance.
     * <p>
     * While being similar to {@link me.fixeddev.commandflow.command.Command.Builder} it allows to create commands based on annotations,
     * that means that the {@link me.fixeddev.commandflow.command.Action} or {@link me.fixeddev.commandflow.part.CommandPart} instances
     * for the build command will be created based on a given method.
     *
     * @param name The name of {@link me.fixeddev.commandflow.command.Command} to build.
     * @return A {@link CommandDataNode} which will allow building a complete {@link me.fixeddev.commandflow.command.Command} tree.
     */
    CommandDataNode newCommand(String name);

    static AnnotatedCommandBuilder create(PartInjector injector) {
        return new AnnotatedCommandBuilderImpl(injector);
    }

}
