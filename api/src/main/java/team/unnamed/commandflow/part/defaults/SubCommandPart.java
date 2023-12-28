package team.unnamed.commandflow.part.defaults;

import team.unnamed.commandflow.Authorizer;
import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.CommandManager;
import team.unnamed.commandflow.ContextSnapshot;
import team.unnamed.commandflow.command.modifiers.FallbackCommandModifiers;
import team.unnamed.commandflow.command.Command;
import team.unnamed.commandflow.command.modifiers.CommandModifiers;
import team.unnamed.commandflow.command.modifiers.ModifierPhase;
import team.unnamed.commandflow.exception.ArgumentException;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.exception.InvalidSubCommandException;
import team.unnamed.commandflow.exception.NoMoreArgumentsException;
import team.unnamed.commandflow.exception.NoPermissionsException;
import team.unnamed.commandflow.exception.StopParseException;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.part.visitor.CommandPartVisitor;
import team.unnamed.commandflow.stack.ArgumentStack;
import team.unnamed.commandflow.stack.StackSnapshot;
import net.kyori.adventure.text.Component;

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
        Component component;

        if (isOptional()) {
            component = Component.text("[" + getName() + "]");
        } else {
            component = Component.text("<" + getName() + ">");
        }

        return component;
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack, CommandPart caller) throws ArgumentParseException {
        String label;
        try {
            label = stack.next();
        } catch (NoMoreArgumentsException e) {
            if (optional) {
                return;
            }

            throw e;
        }

        Command command = subCommands.get(label.toLowerCase());

        StackSnapshot snapshot = stack.getSnapshot();
        ContextSnapshot contextSnapshot = context.getSnapshot();

        try {
            context.setRaw(this, Collections.singletonList(label));
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
        String next = stack.hasNext() ? stack.next() : null;

        if (next == null) {
            // No next argument, this part shouldn't be parsed yet
            return Collections.emptyList();
        }

        Command command = subCommands.get(next);

        List<String> suggestions = new ArrayList<>();

        // Should be there
        CommandManager manager = commandContext.getObject(CommandManager.class, "commandManager");
        Authorizer authorizer = manager.getAuthorizer();

        Map<Command, Boolean> testedCommands = new HashMap<>();

        subCommands.forEach((name, subCommand) -> {
            if (name.startsWith(next)) {
                if (testedCommands.computeIfAbsent(subCommand, c -> authorizer.isAuthorized(commandContext, subCommand.getPermission()))) {
                    suggestions.add(name);
                }
            }
        });

        if (stack.hasNext() && command != null) {
            return command.getPart().getSuggestions(commandContext, stack);
        }

        return suggestions;
    }

    @Override
    public <T> T acceptVisitor(CommandPartVisitor<T> visitor) {
        return visitor.visit(this);
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
                throw new InvalidSubCommandException(Component.translatable("command.subcommand.invalid", Component.text(label)))
                        .setArgument(context.getPart())
                        .setCommand(commandContext.getCommand());
            }

            // Should be there
            CommandManager manager = commandContext.getObject(CommandManager.class, "commandManager");

            if (!manager.getAuthorizer().isAuthorized(commandContext, command.getPermission())) {
                throw new NoPermissionsException(command.getPermissionMessage())
                        .setCommand(command);
            }


            FallbackCommandModifiers fallbackModifiers = manager.getCommandModifiers();

            commandContext.setCommand(command, label);
            CommandModifiers modifiers = command.getModifiers();

            if (!modifiers.hasModifiers(ModifierPhase.PRE_PARSE)) {
                if (!fallbackModifiers.callModifiers(ModifierPhase.PRE_PARSE, commandContext, stack)) {
                    // we just want to stop here if the pre-parse modifiers return false
                    throw new StopParseException();
                }
            } else {
                if (!modifiers.callModifiers(ModifierPhase.PRE_PARSE, commandContext, stack)) {
                    // we just want to stop here if the pre-parse modifiers return false
                    throw new StopParseException();
                }
            }

            command.getPart().parse(commandContext, stack, command.getPart());
        }
    }
}
