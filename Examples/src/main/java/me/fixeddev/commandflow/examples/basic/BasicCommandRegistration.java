package me.fixeddev.commandflow.examples.basic;

import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.NamespaceImpl;
import me.fixeddev.commandflow.SimpleCommandManager;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.input.QuotedSpaceTokenizer;

public class BasicCommandRegistration {

    public static void main(String[] args) {
        // see CommandManagerCreation
        CommandManager commandManager = new SimpleCommandManager();

        commandManager.setInputTokenizer(new QuotedSpaceTokenizer());

        // We can register a command using CommandManager#register
        // To create a command we can use Command#builder(String)

        // This will create a command with name test which prints "hi"
        Command testUserCommand = Command.builder("test")
                .action(context -> {
                    System.out.println("Hi");
                }).build();

        // Register the command instance into the command manager
        commandManager.registerCommand(testUserCommand);

        // With this we execute a command
        commandManager.execute(new NamespaceImpl(), "test");
    }
}
