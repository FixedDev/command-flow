package team.unnamed.commandflow.part.visitor;

import team.unnamed.commandflow.part.PartsWrapper;
import team.unnamed.commandflow.part.defaults.FirstMatchPart;
import team.unnamed.commandflow.part.defaults.SequentialCommandPart;

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
