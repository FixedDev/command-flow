package me.fixeddev.commandflow.examples.basic;

import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.SimpleCommandManager;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.examples.user.User;
import me.fixeddev.commandflow.input.QuotedSpaceTokenizer;
import me.fixeddev.commandflow.part.CommandPart;

import static me.fixeddev.commandflow.part.Parts.*;

public class CommandWithArgumentsRegistration {

    public static void main(String[] args) {
        // see CommandManagerCreation
        CommandManager commandManager = create();
        register(commandManager);

        // See NamespaceUsage
        // Those are the injected arguments for the command
        Namespace namespace = Namespace.create();
        namespace.setObject(User.class, "USER", new User("Fixed", 16));

        // see CommandExecution
        commandManager.execute(namespace, "test Fixed false"); // Prints Hi Fixed
        commandManager.execute(namespace, "test Fixed true"); // Prints Goodbye Fixed
        commandManager.execute(namespace, "test Fixed"); // Throws a NoMoreArguments exception, meaning that the parsing failed
                                                             // due to missing arguments.
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

        // What is a part? A part said in a simple way is an argument of a command.
        // It can take any number of string arguments of a command and convert them into an object.
        // That means that you can have any type of argument, you just need a part that converts the
        // string representation of your argument into a valid object.

        // Here we create a StringPart(String argument) with the name "name"
        CommandPart name = string("name");

        // Here we create a BooleanPart(boolean argument) with the name "goodBye"
        CommandPart goodByePart = booleanPart("goodBye");

        Command testUserCommand = Command
                .builder("test")
                // Here we add a part into the Command
                .addPart(name)
                // You can add multiple parts into a command
                // They will be added into a main part called SequentialCommandPart
                // Which will call every part of the Command in a sequence to parse every argument.
                .addPart(goodByePart)
                .action(context -> {
                    // The values for a Part(argument) may not be present, check if they're
                    // before trying to use them
                    boolean goodBye = context.<Boolean>getValue(goodByePart).orElse(false);

                    context.<String>getValue(name).ifPresent(s -> {
                        if (goodBye) {
                            System.out.println("Goodbye " + s);

                            return;
                        }

                        System.out.println("Hi " + s);
                    });
                })
                .build();

        manager.registerCommand(testUserCommand);
    }
}
