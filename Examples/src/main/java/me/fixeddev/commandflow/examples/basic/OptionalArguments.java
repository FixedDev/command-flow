package me.fixeddev.commandflow.examples.basic;

import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.SimpleCommandManager;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.examples.user.User;
import me.fixeddev.commandflow.input.QuotedSpaceTokenizer;
import me.fixeddev.commandflow.part.CommandPart;

import java.util.Arrays;
import java.util.Collections;

import static me.fixeddev.commandflow.part.Parts.*;

public class OptionalArguments {

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
        commandManager.execute(namespace, "test true Fixed"); // Prints Goodbye Fixed
        commandManager.execute(namespace, "test true"); // Prints Goodbye User
        commandManager.execute(namespace, "test"); // Prints Hi User
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

        CommandPart goodByeValue = booleanPart("goodBye");
        // This makes the goodByeValue argument optional, with the default value "false"
        CommandPart goodBye = optional(goodByeValue, Collections.singletonList("false"));

        CommandPart nameValue = string("name");
        // This makes the nameValue argument optional, without a default value
        CommandPart name = optional(nameValue);


        Command testUserCommand = Command
                .builder("test")
                .addPart(goodBye)
                .addPart(name)
                .action(context -> {
                    String nameString = context.<String>getValue(nameValue).orElse("User");

                    // Should be present every time, since it has a default value
                    // If the default value is not valid for the specified part, then the argument could be absent
                    if (context.<Boolean>getValue(goodByeValue).get()) {
                        System.out.println("Goodbye " + nameString);

                        return;
                    }


                    System.out.println("Hi " + nameString);

                })
                .build();

        manager.registerCommand(testUserCommand);
    }
}
