package team.unnamed.commandflow.part.visitor;

import team.unnamed.commandflow.part.ArgumentPart;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.part.PartsWrapper;
import team.unnamed.commandflow.part.SinglePartWrapper;
import team.unnamed.commandflow.part.defaults.SubCommandPart;

public interface CommandPartVisitor<R> {

    R visit(CommandPart part);

    R visit(ArgumentPart argumentPart);

    R visit(PartsWrapper partsWrapper);

    R visit(SinglePartWrapper singlePartWrapper);

    R visit(SubCommandPart subCommandPart);

}
