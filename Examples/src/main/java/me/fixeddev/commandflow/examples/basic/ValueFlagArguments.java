package me.fixeddev.commandflow.examples.basic;

import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.SimpleCommandManager;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.examples.user.User;
import me.fixeddev.commandflow.input.QuotedSpaceTokenizer;
import me.fixeddev.commandflow.part.CommandPart;

import static me.fixeddev.commandflow.part.Parts.*;

public class ValueFlagArguments {

    public static void main(String[] args) {
        // see CommandManagerCreation
        CommandManager commandManager = create();
        register(commandManager);

        // See NamespaceUsage
        // Those are the injected arguments for the command
        Namespace namespace = Namespace.create();
        namespace.setObject(User.class, "USER", new User("Fixed", 16));

        // see CommandExecution
        commandManager.execute(namespace, "test Fixed"); // Prints Hi Fixed
        commandManager.execute(namespace, "test Fixed -g GoodBye"); // Prints GoodBye Fixed
        commandManager.execute(namespace, "test Fixed -g Hello"); // Prints Hello Fixed

        System.out.println(String.join(",", commandManager.getSuggestions(namespace, "test -g")));

        commandManager.execute(namespace, "test -g Fixed"); // Throws a NoMoreArguments exception, meaning that the parsing failed
        // because the Fixed argument was taken as the value for the flag and no argument
        // is remaining for the name.
        commandManager.execute(namespace, "test Fixed -g"); // Throws a NoMoreArguments exception, meaning that the parsing failed
        // because the flag doesn't has any argument left to use.

    }

    private static CommandManager create() {
        // see CommandManagerCreation
        CommandManager commandManager = new SimpleCommandManager();
        commandManager.setInputTokenizer(new QuotedSpaceTokenizer());

        return commandManager;
    }

    private static void register(CommandManager manager) {
        // See BasicCommandRegistration
        // See CommandContextUsage
        // See CommandWithArgumentsRegistration
        // See SwitchArguments

        CommandPart name = string("name");

        // This part is like a switch part, the difference is that when the main "switch" is found
        // the part provided in the first argument takes the parsing, consuming one or more arguments
        // from the stack at that position.
        CommandPart greetingValue = string("greeting");
        CommandPart greetingValueFlag = valueFlag(greetingValue, "g");

        Command testUserCommand = Command
                .builder("test")
                .addPart(greetingValueFlag)
                .addPart(name)
                .action(context -> {
                    // The value for a value flag can be absent
                    String greeting = context.<String>getValue(greetingValue).orElse("Hi");

                    context.<String>getValue(name).ifPresent(s -> {
                        System.out.println(greeting + " " + s);
                    });
                })
                .build();

        manager.registerCommand(testUserCommand);
    }
}
