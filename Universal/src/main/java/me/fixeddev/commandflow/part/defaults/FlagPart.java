package me.fixeddev.commandflow.part.defaults;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import me.fixeddev.commandflow.stack.StackSnapshot;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import org.jetbrains.annotations.Nullable;

public class FlagPart implements CommandPart {

    private String name;
    private String shortName;
    private boolean allowFullName;

    public FlagPart(String name, String shortName, boolean allowFullName) {
        this.name = name;
        this.shortName = shortName;
        this.allowFullName = allowFullName;
    }

    public FlagPart(String shortName) {
        this.name = shortName;
        this.shortName = shortName;
        this.allowFullName = false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public @Nullable Component getLineRepresentation() {
        TextComponent.Builder builder = TextComponent.builder("[");
        builder.append("-" + name);
        builder.append("]");

        return builder.build();
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack) throws ArgumentParseException {
        StackSnapshot snapshot = stack.getSnapshot();

        boolean found = false;

        while (stack.hasNext()) {
            String arg = stack.next();

            if (!arg.startsWith("-")) {
                continue;
            }

            if (arg.equals("--" + name) && allowFullName) {
                stack.remove();

                context.setValue(this, true);
                found = true;

                break;
            }

            if (arg.equals("-" + shortName)) {
                stack.remove();

                context.setValue(this, true);
                found = true;

                break;
            }
        }

        if (!found) {
            context.setValue(this, false);
        }

        stack.applySnapshot(snapshot, false);
    }
}
