package me.fixeddev.commandflow.annotated;

import me.fixeddev.commandflow.annotated.builder.AnnotatedCommandBuilder;
import me.fixeddev.commandflow.annotated.part.PartInjector;
import me.fixeddev.commandflow.command.Command;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.function.Function;

/**
 * This class has the purpose of creating entire {@linkplain Command} trees based completely on annotations
 * and methods.
 */
public interface AnnotatedCommandTreeBuilder {

    /**
     * Creates a {@linkplain Command} tree from the given {@link CommandClass} instance.
     * The {@link Command} instances created from annotated methods should be public instance methods(not static ones) that return boolean or void,
     * not being inherited from other classes.
     * <p>
     * The {@link me.fixeddev.commandflow.annotated.annotation.Command} annotation is used to indicate whether a method or a class is a Command,
     * if a class has this annotation, that means that every command in that class is his subcommand, with the main command being
     * a command with empty name.
     * <p>
     * The {@link me.fixeddev.commandflow.annotated.annotation.Handler} annotation is used to indicate that a method of a class with subcommands is the one that
     * handles the sub commands, actualy being a {@link me.fixeddev.commandflow.part.defaults.SubCommandPart.SubCommandHandler} but created with
     * an annotated method.
     * <p>
     * The {@link me.fixeddev.commandflow.annotated.annotation.SubCommandClasses} annotation is  used to indicate that the given classes are actually subcommands
     * of the given {@link CommandClass}, these sub command classes will be instantiated using a {@link SubCommandInstanceCreator} and
     * parsed with this same method, the returned commands will be added as subcommands for the provided {@link CommandClass}.
     * This allows multiple levels of subcommands without a lot of hassle.
     *
     * @param commandClass The {@link CommandClass} instance of this command tree.
     * @return A not null list of {@link Command} containing the main level commands which are on this class.
     */
    List<Command> fromClass(CommandClass commandClass);

    /**
     * Sets the function responsible for converting from string to {@link Component}.
     *
     * <p>The function is used to convert values provided in the annotations, to values
     * that the {@link Command} interface accepts.</p>
     *
     * @param parser The parser function
     * @since 1.0.0
     */
    void setComponentParser(Function<String, Component> parser);

    static AnnotatedCommandTreeBuilder create(AnnotatedCommandBuilder builder, SubCommandInstanceCreator instanceCreator) {
        return new AnnotatedCommandTreeBuilderImpl(builder, instanceCreator);
    }

    static AnnotatedCommandTreeBuilder create(PartInjector injector, SubCommandInstanceCreator instanceCreator) {
        return create(AnnotatedCommandBuilder.create(injector), instanceCreator);
    }

    static AnnotatedCommandTreeBuilder create(PartInjector injector) {
        return new AnnotatedCommandTreeBuilderImpl(injector);
    }

}




