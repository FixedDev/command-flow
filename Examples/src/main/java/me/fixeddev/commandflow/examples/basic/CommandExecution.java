package me.fixeddev.commandflow.examples.basic;

import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.NamespaceImpl;
import me.fixeddev.commandflow.SimpleCommandManager;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.examples.user.User;
import me.fixeddev.commandflow.input.QuotedSpaceTokenizer;

public class CommandExecution {

    public static void main(String[] args) {
        // see CommandManagerCreation
        CommandManager commandManager = create();
        register(commandManager);

        // See NamespaceUsage
        // Those are the injected arguments for the command
        Namespace namespace = Namespace.create();
        namespace.setObject(User.class, "USER", new User("Fixed", 16));

        // Ok, this is the easiest thing to understand.
        // Those are just the arguments for the command, including the name of the command
        // We can use the arguments as a String to allow the InputTokenizer to split it into tokens.
        // Or directly pass the arguments as List<String>
        String line = "test";

        // Here we execute the command "test" with the user injected.
        commandManager.execute(namespace, line);
    }

    private static CommandManager create() {
        // see CommandManagerCreation
        CommandManager commandManager = new SimpleCommandManager();
        commandManager.setInputTokenizer(new QuotedSpaceTokenizer());

        return commandManager;
    }

    private static void register(CommandManager manager) {
        // See BasicCommandRegistration
        Command testUserCommand = Command.builder("test")
                .action(context -> {
                    System.out.println("Hi");
                }).build();

        manager.registerCommand(testUserCommand);
    }

}
