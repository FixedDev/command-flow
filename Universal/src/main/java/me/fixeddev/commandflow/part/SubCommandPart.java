package me.fixeddev.commandflow.part;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.stack.ArgumentStack;
import net.kyori.text.TextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SubCommandPart implements CommandPart {

    public static final SubCommandHandler DEFAULT_HANDLER = new DefaultSubCommandHandler();

    private final String name;
    private final Map<String, Command> subCommands;
    private final Set<Command> subCommandsSet;
    private final SubCommandHandler handler;

    public SubCommandPart(String name, Collection<Command> subCommands, SubCommandHandler handler) {
        this.name = name;
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

    public SubCommandPart(String name, Collection<Command> subCommands) {
        this(name, subCommands, DEFAULT_HANDLER);
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack) throws ArgumentParseException {
        String label = stack.next();
        Command command = subCommands.get(label);

        handler.handle(this, context, label, command);
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

    public interface SubCommandHandler {
        void handle(@NotNull SubCommandPart part, @NotNull CommandContext commandContext, @NotNull String label, @Nullable Command command) throws ArgumentParseException;
    }

    public static class DefaultSubCommandHandler implements SubCommandHandler {

        @Override
        public void handle(@NotNull SubCommandPart part, @NotNull CommandContext commandContext, @NotNull String label, @Nullable Command command) throws ArgumentParseException {
            if (command == null) {
                // TODO: Set an actual translatable message
                throw new ArgumentParseException(TextComponent.of("The subcommand " + label + " doesn't exists!"));
            }

            commandContext.setCommand(command, label);
        }
    }
}
