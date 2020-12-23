package me.fixeddev.commandflow.examples.basic;

import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.SimpleCommandManager;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.examples.user.User;
import me.fixeddev.commandflow.input.QuotedSpaceTokenizer;
import me.fixeddev.commandflow.part.CommandPart;

import static me.fixeddev.commandflow.part.Parts.*;

public class SwitchArguments {

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
        commandManager.execute(namespace, "test -g Fixed"); // Prints Goodbye Fixed
        commandManager.execute(namespace, "test Fixed -g"); // Prints Goodbye Fixed
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
        // See CommandWithArgumentsRegistration

        CommandPart name = string("name");
        // If the -g argument is present, then the switch value will be true, otherwise false
        // It can be in any position, but the parts registered before will take priority, that means that if the name part is registered
        // before the switch part, the -g only can be after the name
        CommandPart goodByeSwitch = switchPart("goodBye", "g");

        Command testUserCommand = Command
                .builder("test")
                .addPart(goodByeSwitch)
                .addPart(name)
                .action(context -> {
                    // The value for a switch is never absent
                    boolean goodBye = context.<Boolean>getValue(goodByeSwitch).get();

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
