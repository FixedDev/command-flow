package me.fixeddev.commandflow.minestom;

import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.NamespaceImpl;
import me.fixeddev.commandflow.exception.CommandException;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.part.PartsWrapper;
import me.fixeddev.commandflow.part.defaults.BooleanPart;
import me.fixeddev.commandflow.part.defaults.DoublePart;
import me.fixeddev.commandflow.part.defaults.FloatPart;
import me.fixeddev.commandflow.part.defaults.IntegerPart;
import me.fixeddev.commandflow.part.defaults.LongPart;
import me.fixeddev.commandflow.part.defaults.SubCommandPart;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.suggestion.SuggestionCallback;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.minestom.server.command.builder.arguments.ArgumentType.*;

public class MinestomCommandWrapper extends Command {

    private final SuggestionCallback suggestionCallback;
    private final CommandManager commandManager;

    public MinestomCommandWrapper(
            @NotNull me.fixeddev.commandflow.command.Command command,
            @NotNull CommandManager commandManager,
            @NotNull String name,
            @Nullable String... aliases
    ) {
        super(name, aliases);

        this.suggestionCallback = (sender, context, suggestion) -> {
            Namespace namespace = new NamespaceImpl();
            namespace.setObject(CommandSender.class, MinestomCommandManager.SENDER_NAMESPACE, sender);

            List<String> suggestions = commandManager.getSuggestions(namespace, context.getInput());
            suggestions.forEach(s -> suggestion.addEntry(new SuggestionEntry(s)));
        };
        this.commandManager = commandManager;

        collectAllCommands(command).forEach(this::addSyntaxForCommand);
    }

    @Override
    public void globalListener(@NotNull CommandSender sender, @NotNull CommandContext context, @NotNull String command) {
        Namespace namespace = new NamespaceImpl();
        namespace.setObject(CommandSender.class, MinestomCommandManager.SENDER_NAMESPACE, sender);

        try {
            commandManager.execute(namespace, command);
        } catch (CommandException e) {
            if (e.getCause() instanceof CommandException) {
                throw (CommandException) e.getCause();
            }

            sendMessageToSender(e, namespace);

            throw new CommandException("An unexpected exception occurred while executing the command " + command, e);
        }
    }

    public static void sendMessageToSender(CommandException exception, Namespace namespace) {
        CommandManager commandManager = namespace.getObject(CommandManager.class, "commandManager");
        CommandSender sender = namespace.getObject(CommandSender.class, MinestomCommandManager.SENDER_NAMESPACE);

        Component component = exception.getMessageComponent();
        Component translatedComponent = commandManager.getTranslator().translate(component, namespace);

        sender.sendMessage(translatedComponent);
    }

    private static Argument<?> chooseArgumentByPart(CommandPart part) {
        String name = part.getName();
        if (part instanceof DoublePart) {
            return Double(name);
        }

        if (part instanceof LongPart) {
            return Long(name);
        }

        if (part instanceof IntegerPart) {
            return Integer(name);
        }

        if (part instanceof FloatPart) {
            return Float(name);
        }

        if (part instanceof BooleanPart) {
            return Double(name);
        }

        return String(name);
    }

    private List<me.fixeddev.commandflow.command.Command> collectAllCommands(me.fixeddev.commandflow.command.Command command) {
        if (command.getPart() instanceof SubCommandPart) {
            return new ArrayList<>(((SubCommandPart) command.getPart()).getSubCommands());
        }

        return Collections.singletonList(command);
    }

    private void addSyntaxForCommand(me.fixeddev.commandflow.command.Command command) {
        CommandPart part = command.getPart();

        List<CommandPart> parts;
        Argument<?>[] arguments;

        if (part instanceof PartsWrapper) {
            parts = ((PartsWrapper) part).getParts();
        } else {
            parts = Collections.singletonList(part);
        }

        arguments = new Argument[parts.size() + 1];
        arguments[0] = Literal(command.getName());

        for (int i = 0; i < parts.size(); i++) {
            CommandPart commandPart = parts.get(i);
            Argument<?> argument = arguments[i + 1] = chooseArgumentByPart(commandPart);

            argument.setSuggestionCallback(suggestionCallback);
        }

        addSyntax((sender, context) -> {}, arguments);
    }
}