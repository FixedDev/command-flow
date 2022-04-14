package me.fixeddev.commandflow.part.defaults;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;

/**
 * A {@linkplain CommandPart} that provides direct access to the {@linkplain CommandContext}.
 */
public class ContextPart implements CommandPart {

    private final String name;

    public ContextPart(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack, CommandPart parent) throws ArgumentParseException {
        context.setValue(this, context);
    }

}
