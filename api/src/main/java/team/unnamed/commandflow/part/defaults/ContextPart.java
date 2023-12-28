package team.unnamed.commandflow.part.defaults;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.stack.ArgumentStack;

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
