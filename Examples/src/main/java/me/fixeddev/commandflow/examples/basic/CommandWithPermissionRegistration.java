package me.fixeddev.commandflow.examples.basic;

import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.NamespaceImpl;
import me.fixeddev.commandflow.SimpleCommandManager;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.examples.user.User;
import me.fixeddev.commandflow.examples.user.UserAuthorizer;
import me.fixeddev.commandflow.input.QuotedSpaceTokenizer;

public class CommandWithPermissionRegistration {

    public static void main(String[] args) {
        // see CommandManagerCreation
        // see NamespaceUsage
        // see BasicCommandRegistration
        // see CommandExecution

        CommandManager commandManager = new SimpleCommandManager();
        commandManager.setInputTokenizer(new QuotedSpaceTokenizer());
        commandManager.setAuthorizer(new UserAuthorizer());

        Namespace namespace = Namespace.create();
        namespace.setObject(User.class, "USER", new User("Fixed", 16));

        Command testUserCommand = Command.builder("test")
                // We set the permission of the test command into admin
                .permission("admin")
                .action(context -> {
                    System.out.println("Hi");
                }).build();

        commandManager.registerCommand(testUserCommand);
        commandManager.execute(namespace, "test"); // Throws NoPermissionException
    }
}
