package me.fixeddev.commandflow.minestom;

import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.NamespaceImpl;
import me.fixeddev.commandflow.exception.CommandException;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MinestomCommandWrapper extends Command {

    private final CommandManager commandManager;

    public MinestomCommandWrapper(
            @NotNull CommandManager commandManager,
            @NotNull String name,
            @Nullable String... aliases
    ) {
        super(name, aliases);

        addSyntax((sender, context) -> {}, new FakeArgument(commandManager));
        this.commandManager = commandManager;
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
}