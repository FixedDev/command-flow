package team.unnamed.commandflow.annotated.builder;

import team.unnamed.commandflow.annotated.part.PartInjector;
import team.unnamed.commandflow.command.Action;
import team.unnamed.commandflow.command.Command;
import team.unnamed.commandflow.part.CommandPart;

public interface AnnotatedCommandBuilder {

    /**
     * Creates a new builder to create a {@link Command} instance.
     * <p>
     * While being similar to {@link Command.Builder} it allows to create commands based on annotations,
     * that means that the {@link Action} or {@link CommandPart} instances
     * for the build command will be created based on a given method.
     *
     * @param name The name of {@link Command} to build.
     * @return A {@link CommandDataNode} which will allow building a complete {@link Command} tree.
     */
    CommandDataNode newCommand(String name);

    static AnnotatedCommandBuilder create(PartInjector injector) {
        return new AnnotatedCommandBuilderImpl(injector);
    }

}
