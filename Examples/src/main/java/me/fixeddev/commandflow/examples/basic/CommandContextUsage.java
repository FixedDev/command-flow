package me.fixeddev.commandflow.examples.basic;

import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.SimpleCommandManager;
import me.fixeddev.commandflow.command.Action;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.examples.user.User;
import me.fixeddev.commandflow.input.QuotedSpaceTokenizer;

public class CommandContextUsage {

    public static void main(String[] args) {
        // see CommandManagerCreation
        CommandManager commandManager = create();

        Action action = context -> {
            // Here we have an action of a command, and here we can use the context for this command
            // The CommandContext is the result of parsing the arguments of a command.
            // Also it extends Namespace, so you can use the Namespace methods on it.

            // The labels are the names for every command/subcommand executed.
            // This is the name of the last subcommand/command
            String label = context.getLabels().get(context.getLabels().size() - 1);

            System.out.println("Label: " + label);

            return true;
        };

        register(commandManager, action);

        // See NamespaceUsage
        // Those are the injected arguments for the command
        Namespace namespace = Namespace.create();
        namespace.setObject(User.class, "USER", new User("Fixed", 16));

        // see CommandExecution
        String line = "test";
        commandManager.execute(namespace, line); // Prints Label: test
        commandManager.execute(namespace, "test2"); // Prints Label: test2

    }

    private static CommandManager create() {
        // see CommandManagerCreation
        CommandManager commandManager = new SimpleCommandManager();
        commandManager.setInputTokenizer(new QuotedSpaceTokenizer());

        return commandManager;
    }

    private static void register(CommandManager manager, Action action) {
        // See BasicCommandRegistration
        Command testUserCommand = Command
                .builder("test")
                .addAlias("test2")
                .action(action).build();

        manager.registerCommand(testUserCommand);
    }
}
