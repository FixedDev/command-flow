package me.fixeddev.commandflow.part.defaults;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.part.visitor.CommandPartVisitor;
import me.fixeddev.commandflow.stack.ArgumentStack;
import net.kyori.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArgumentRewriterPart implements CommandPart {

    private final CommandPart delegatePart;
    private final Map<String, String> rewrites;

    public ArgumentRewriterPart( CommandPart delegatePart) {
        this.delegatePart = delegatePart;

        rewrites = new HashMap<>();
    }

    @Override
    public String getName() {
        return delegatePart.getName();
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack, @Nullable CommandPart caller) throws ArgumentParseException {
        String next = stack.next();
        int position = stack.getPosition();

        String to = rewrites.get(next);

        if (to != null) {
            stack.getBacking().set(position, to);
        }

        delegatePart.parse(context, stack, caller);
    }

    @Override
    public @Nullable Component getLineRepresentation() {
        return delegatePart.getLineRepresentation();
    }

    @Override
    public @Nullable List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        return delegatePart.getSuggestions(commandContext, stack);
    }

    @Override
    public boolean isAsync() {
        return delegatePart.isAsync();
    }

    @Override
    public <T> T acceptVisitor(CommandPartVisitor<T> visitor) {
        return delegatePart.acceptVisitor(visitor);
    }

    public void addRewrite(String to, String... from) {
        for (String fromStr : from) {
            rewrites.put(fromStr, to);
        }
    }

    public void removeRewrite(String to, String... from) {
        for (String fromStr : from) {
            rewrites.remove(fromStr, to);
        }
    }

}
