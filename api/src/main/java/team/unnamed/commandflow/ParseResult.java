package team.unnamed.commandflow;

import team.unnamed.commandflow.exception.CommandException;

import java.util.Optional;

public interface ParseResult {

    Optional<CommandContext> getContext();

    Optional<CommandException> getException();

    default boolean isError() {
        return getException().isPresent();
    }

}
