package me.fixeddev.commandflow.part.visitor;

import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.part.PartsWrapper;
import me.fixeddev.commandflow.part.SinglePartWrapper;
import me.fixeddev.commandflow.part.defaults.SubCommandPart;

public interface CommandPartVisitor<R> {

    R visit(CommandPart part);

    R visit(ArgumentPart argumentPart);

    R visit(PartsWrapper partsWrapper);

    R visit(SinglePartWrapper singlePartWrapper);

    R visit(SubCommandPart subCommandPart);

}
