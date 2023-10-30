package team.unnamed.commandflow.annotated.builder;

import team.unnamed.commandflow.command.Command;

public interface Buildable {

    /**
     * Builds a command based on the internal state of this class.
     *
     * @return A non null {@link Command} instance.
     */
    Command build();

}
