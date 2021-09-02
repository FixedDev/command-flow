package me.fixeddev.commandflow.examples.advanced;

import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.SimpleCommandManager;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilder;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilderImpl;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Named;
import me.fixeddev.commandflow.annotated.part.PartInjector;
import me.fixeddev.commandflow.annotated.part.defaults.DefaultsModule;
import me.fixeddev.commandflow.input.QuotedSpaceTokenizer;

import java.util.List;

public class AnnotationCommands {

    public static void main(String[] args) {
        // see CommandManagerCreation
        CommandManager commandManager = createCommandManager();
        // Now, apart from the basic commands created with the builder api
        // There is other type of commands. The commands based on annotations.
        // The first thing to use them is to create a PartInjector.

        // The PartInjector is a registry which holds the registered PartFactories
        // and PartModifiers.
        //
        // A PartFactory is a class that provides a way to create an specific type of part.
        // A PartModifier is like a PartFactory, the difference is that it may wrap the original part
        // or modify it instead of creating a new one.
        PartInjector injector = PartInjector.create();
        // We need to install the default bindings first(String, Boolean, Double, Float, Integer, Text(String), ArgumentStack, CommandContext)
        // The module also includes the next modifiers(LimitModifier, OptionalModifier, ValueFlagModifier)
        injector.install(new DefaultsModule());

        // This class is the one that actually converts a class full of methods and annotations into a list of commands.
        // This is the most basic way to create it
        // To use this class we need a class implementing CommandClass(just a marker interface)
        // Then we call to call AnnotatedTreeBuilder#fromClass(CommandClass) with that class instance.
        AnnotatedCommandTreeBuilder treeBuilder = new AnnotatedCommandTreeBuilderImpl(injector);

        // Here we call the treeBuilder with our TestCommands class, the returned list contains all the commands that
        // were defined in the provided instance.
        List<me.fixeddev.commandflow.command.Command> commands = treeBuilder.fromClass(new TestCommands());

        // see BasicCommandRegistration
        // Register them normally
        commandManager.registerCommands(commands);

        commandManager.execute(Namespace.create(), "test Fixed"); // Prints "Hi Fixed"
        commandManager.execute(Namespace.create(), "test"); // Throws NoMoreArgumentsException since the name argument is missing
    }

    private static CommandManager createCommandManager() {
        // see CommandManagerCreation
        CommandManager commandManager = new SimpleCommandManager();
        commandManager.setInputTokenizer(new QuotedSpaceTokenizer());

        return commandManager;
    }

    private static class TestCommands implements CommandClass {

        // Here we create a command called "test"
        // With a String argument called "name"
        // It doesn't need to return boolean, it can be void but, the method should be public and not static.
        @Command(names = "test")
        public boolean test(@Named("name") String name) {
            System.out.println("Hi " + name);

            return true;
        }
    }

}
