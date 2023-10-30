package me.fixeddev.commandflow.part.visitor;

import me.fixeddev.commandflow.part.PartsWrapper;
import me.fixeddev.commandflow.part.defaults.FirstMatchPart;
import me.fixeddev.commandflow.part.defaults.SequentialCommandPart;

public interface UnwrappedCommandPartVisitor<R> extends CommandPartVisitor<R> {

    @Override
    default R visit(PartsWrapper partsWrapper) {
        if (partsWrapper instanceof SequentialCommandPart) {
            return visitSequential((SequentialCommandPart) partsWrapper);
        }

        if (partsWrapper instanceof FirstMatchPart) {
            return visitFirstMatch((FirstMatchPart) partsWrapper);
        }

        return visitWrapper(partsWrapper);
    }

    R visitWrapper(PartsWrapper partsWrapper);

    R visitSequential(SequentialCommandPart sequentialCommandPart);

    R visitFirstMatch(FirstMatchPart firstMatchPart);

}
