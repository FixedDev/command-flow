package me.fixeddev.commandflow.part.defaults;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.ContextSnapshot;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.exception.ArgumentException;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.exception.InvalidSubCommandException;
import me.fixeddev.commandflow.exception.NoPermissionsException;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import me.fixeddev.commandflow.stack.StackSnapshot;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.TranslatableComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SubCommandPart implements CommandPart {

    public static final SubCommandHandler DEFAULT_HANDLER = new DefaultSubCommandHandler();

    private final String name;
    private final Map<String, Command> subCommands;
    private final Set<Command> subCommandsSet;
    private final SubCommandHandler handler;

    private boolean optional;

    public SubCommandPart(String name, Collection<Command> subCommands, boolean optional, SubCommandHandler handler) {
        this.name = name;
        this.optional = optional;
        this.handler = handler;
        this.subCommands = new HashMap<>();
        subCommandsSet = new HashSet<>(subCommands);

        for (Command subCommand : subCommands) {
            this.subCommands.put(subCommand.getName(), subCommand);

            for (String alias : subCommand.getAliases()) {
                this.subCommands.put(alias, subCommand);
            }
        }
    }

    public SubCommandPart(String name, Collection<Command> subCommands, boolean optional) {
        this(name, subCommands, optional, DEFAULT_HANDLER);
    }

    public SubCommandPart(String name, Collection<Command> subCommands) {
        this(name, subCommands, false);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public @Nullable Component getLineRepresentation() {
        TextComponent.Builder builder = TextComponent.builder().content("<" + getName() + ">");

        return builder.build();
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack) throws ArgumentParseException {
        String label = stack.next();
        Command command = subCommands.get(label);

        StackSnapshot snapshot = stack.getSnapshot();
        ContextSnapshot contextSnapshot = context.getSnapshot();

        try {
            handler.handle(new DefaultHandlerContext(this, context, stack), label, command);
        } catch (ArgumentException e) {
            if (optional && (e instanceof InvalidSubCommandException)) {
                stack.applySnapshot(snapshot);
                context.applySnapshot(contextSnapshot);

                return;
            }

            throw e;
        }
    }

    @Override
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        String next = stack.hasNext() ? stack.next() : "";
        Command command = subCommands.get(next);

        if (command != null) {
            return command.getPart().getSuggestions(commandContext, stack);
        }

        List<String> suggestions = new ArrayList<>();

        for (String subcommand : subCommands.keySet()) {
            if (subcommand.startsWith(next)) {
                suggestions.add(subcommand);
            }
        }

        return suggestions;
    }

    public Map<String, Command> getSubCommandMap() {
        return subCommands;
    }

    /**
     * The commands part of this {@link SubCommandPart} with no repeated commands.
     *
     * @return A Collection of the {@link Command} instances contained by this subcommand.
     */
    public Collection<Command> getSubCommands() {
        return subCommandsSet;
    }

    /**
     * If this {@link SubCommandPart} instance is an optional subcommand(that means that if it doens't exist it doens't throw an error)
     *
     * @return If this {@linkplain SubCommandPart} is optional
     */
    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public interface HandlerContext {
        /**
         * The {@link SubCommandPart} that's calling this handler.
         *
         * @return The caller {@link SubCommandPart}.
         */
        @NotNull SubCommandPart getPart();

        /**
         * The {@link CommandContext} of the parent command.
         *
         * @return The {@link CommandContext} of the parent command.
         */
        @NotNull CommandContext getContext();

        /**
         * The {@link ArgumentStack} of the parent {@link SubCommandPart}.
         *
         * @return The {@link ArgumentStack} of the {@link SubCommandPart}.
         */
        @NotNull ArgumentStack getStack();
    }

    private static class DefaultHandlerContext implements HandlerContext {

        private final SubCommandPart part;
        private final CommandContext context;
        private final ArgumentStack stack;

        public DefaultHandlerContext(SubCommandPart part, CommandContext context, ArgumentStack stack) {
            this.part = part;
            this.context = context;
            this.stack = stack;
        }

        @Override
        public @NotNull SubCommandPart getPart() {
            return part;
        }

        @Override
        public @NotNull CommandContext getContext() {
            return context;
        }

        @Override
        public @NotNull ArgumentStack getStack() {
            return stack;
        }
    }

    public interface SubCommandHandler {
        /**
         * Handle the context change from the main command into the sub command and handle the start of the parsing for the {@link CommandPart} of the
         * subcommand.
         *
         * @param context The context for the handler.
         * @param label   The label of the subcommand.
         * @param command The subcommand instance if found, otherwise null.
         * @throws ArgumentException          If an error with the subcommand is encountered.
         * @throws InvalidSubCommandException If the provided label isn't a label for a subcommand of this part.
         */
        void handle(@NotNull HandlerContext context, @NotNull String label, @Nullable Command command) throws ArgumentException;
    }

    public static class DefaultSubCommandHandler implements SubCommandHandler {

        @Override
        public void handle(@NotNull HandlerContext context, @NotNull String label, @Nullable Command command) throws ArgumentException {
            CommandContext commandContext = context.getContext();
            ArgumentStack stack = context.getStack();

            if (command == null) {
                // TODO: Set an actual translatable message
                InvalidSubCommandException commandException = new InvalidSubCommandException(TranslatableComponent.of("command.subcommand.invalid", TextComponent.of(label)));
                commandException.setArgument(context.getPart());
                commandException.setCommand(commandContext.getCommand());

                throw new InvalidSubCommandException(TranslatableComponent.of("command.subcommand.invalid", TextComponent.of(label)));
            }

            // Should be there
            CommandManager manager = commandContext.getObject(CommandManager.class, "commandManager");

            if (!manager.getAuthorizer().isAuthorized(commandContext, command.getPermission())) {
                throw new NoPermissionsException(command.getPermissionMessage());
            }

            commandContext.setCommand(command, label);
            command.getPart().parse(commandContext, stack);
        }
    }
}
