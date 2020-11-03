package me.fixeddev.commandflow.part;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.part.defaults.BooleanPart;
import me.fixeddev.commandflow.part.defaults.DoublePart;
import me.fixeddev.commandflow.part.defaults.FirstMatchPart;
import me.fixeddev.commandflow.part.defaults.FlagPart;
import me.fixeddev.commandflow.part.defaults.FloatPart;
import me.fixeddev.commandflow.part.defaults.IntegerPart;
import me.fixeddev.commandflow.part.defaults.LimitingPart;
import me.fixeddev.commandflow.part.defaults.OptionalPart;
import me.fixeddev.commandflow.part.defaults.SequentialCommandPart;
import me.fixeddev.commandflow.part.defaults.StringPart;
import me.fixeddev.commandflow.part.defaults.SubCommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * An utility class to ease the use of {@link CommandPart} and his sub classes.
 */
public final class Parts {
    /**
     * Creates a part that limits the available arguments for the given {@link CommandPart} into the given limit.
     *
     * @param part  The {@link CommandPart} to limit.
     * @param limit The limit for the specified {@link CommandPart}.
     * @return The given {@link CommandPart} that has a limit in what number of arguments it can take.
     */
    public static CommandPart limit(CommandPart part, int limit) {
        return new LimitingPart(part, limit);
    }

    /**
     * Creates a new {@link FlagPart} that searches non positional boolean arguments with the next format -&lt;shortName&gt;, the value for
     * this part will be true if the argument is present at least one time, false if it isn't.
     * <p>
     * After the argument being found it will be deleted from the {@link ArgumentStack} so it doesn't interfere with other parsed {@link CommandPart}.
     *
     * @param shortName The short name of the {@link FlagPart}, this will be used as the flag name(-&lt;shortName&gt;)
     * @return A {@link CommandPart} that will detect flags on the argument stack and delete them, returning true or false depending weather it's present or not.
     */
    public static CommandPart flagPart(String shortName) {
        return flagPart(shortName, shortName);
    }

    /**
     * Creates a new {@link FlagPart} that searches non positional boolean arguments with the next format -&lt;shortName&gt;, the value for
     * this part will be true if the argument is present at least one time, false if it isn't.
     * <p>
     * After the argument being found it will be deleted from the {@link ArgumentStack} so it doesn't interfere with other parsed {@link CommandPart}.
     *
     * @param name      The full internal name of the {@link FlagPart}.
     * @param shortName The short name of the {@link FlagPart}, this will be used as the flag name(-&lt;shortName&gt;)
     * @return A {@link CommandPart} that will detect flags on the argument stack and delete them, returning true or false depending weather it's present or not.
     */
    public static CommandPart flagPart(String name, String shortName) {
        return flagPart(name, shortName, false);
    }

    /**
     * Creates a new {@link FlagPart} that searches non positional boolean arguments with the format -&lt;shortName&gt; or --&lt;name&gt;, the second one only
     * being allowed if the allowFullNameUse parameter is true; the value for this part will be true if the argument is present
     * at least one time in any of its formats, false if it isn't.
     * <p>
     * After the argument being found it will be deleted from the {@link ArgumentStack} so it doesn't interfere with other parsed {@link CommandPart}.
     *
     * @param name             The full name of the {@link FlagPart}, if the parameter allowFullNameUse is true, this will be allowed as flag name(--&lt;name&gt;).
     * @param shortName        The short name of the {@link FlagPart}, this will be used as the flag name(-&lt;shortName&gt;)
     * @param allowFullNameUse Whether the format --&lt;name&gt; is allowed or not.
     * @return A {@link CommandPart} that will detect flags on the argument stack and delete them, returning true or false depending weather it's present or not.
     */
    public static CommandPart flagPart(String name, String shortName, boolean allowFullNameUse) {
        return new FlagPart(name, shortName, allowFullNameUse);
    }

    /**
     * A basic {@link CommandPart} that takes a string from the {@link me.fixeddev.commandflow.stack.ArgumentStack}
     *
     * @param name The name for this part.
     * @return A {@link CommandPart} with the given name that takes a {@link String} as argument.
     */
    public static CommandPart newStringPart(String name) {
        return new StringPart(name);
    }

    /**
     * A basic {@link CommandPart} that takes a string from the {@link me.fixeddev.commandflow.stack.ArgumentStack}
     *
     * @param name The name for this part.
     * @return A {@link CommandPart} with the given name that takes a {@link Integer} as argument.
     */
    public static CommandPart newIntegerPart(String name) {
        return new IntegerPart(name);
    }

    /**
     * A basic {@link CommandPart} that takes a string from the {@link me.fixeddev.commandflow.stack.ArgumentStack}
     *
     * @param name The name for this part.
     * @return A {@link CommandPart} with the given name that takes a {@link Double} as argument.
     */
    public static CommandPart newDoublePart(String name) {
        return new DoublePart(name);
    }

    /**
     * A basic {@link CommandPart} that takes a string from the {@link me.fixeddev.commandflow.stack.ArgumentStack}
     *
     * @param name The name for this part.
     * @return A {@link CommandPart} with the given name that takes a {@link Boolean} as argument.
     */
    public static CommandPart newBooleanPart(String name) {
        return new BooleanPart(name);
    }

    /**
     * A basic {@link CommandPart} that takes a string from the {@link me.fixeddev.commandflow.stack.ArgumentStack}
     *
     * @param name The name for this part.
     * @return A {@link CommandPart} with the given name that takes a {@link Float} as argument.
     */
    public static CommandPart newFloatPart(String name) {
        return new FloatPart(name);
    }

    /**
     * A {@link CommandPart} that consist on a sequence of other CommandParts.
     * <p>
     * After calling {@link CommandPart#parse(CommandContext, ArgumentStack)} on this {@link CommandPart} every part on
     * this {@link FirstMatchPart} will be parsed until some part parses correctly.
     *
     * @param name  The name for this part.
     * @param parts The sequence of {@link CommandPart} instances that this {@link CommandPart} will delegate to.
     * @return A {@link CommandPart} that consists on a sequence of other CommandParts, using the first one that parsed correctly.
     */
    public static CommandPart firstMatchingPart(String name, CommandPart... parts) {
        return new FirstMatchPart(name, Arrays.asList(parts));
    }

    /**
     * A {@link CommandPart} that consist on a sequence of other CommandParts.
     * <p>
     * After calling {@link CommandPart#parse(CommandContext, ArgumentStack)} on this {@link CommandPart} every part on
     * this {@link FirstMatchPart} will be parsed until some part parses correctly/
     *
     * @param name  The name for this part.
     * @param parts The sequence of {@link CommandPart} instances that this {@link CommandPart} will delegate to.
     * @return A {@link CommandPart} that consists on a sequence of other CommandParts, using the first one that parsed correctly.
     */
    public static CommandPart firstMatchingPart(String name, Collection<CommandPart> parts) {
        return new FirstMatchPart(name, new ArrayList<>(parts));
    }

    /**
     * A {@link CommandPart} that consist on a sequence of other CommandParts.
     * <p>
     * After calling {@link CommandPart#parse(CommandContext, ArgumentStack)} on this {@link CommandPart} every part on
     * this CommandPart will be parsed until all the parts are parsed or an error is thrown
     *
     * @param name  The name for this part.
     * @param parts The sequence of {@link CommandPart} instances that this {@link CommandPart} will delegate to.
     * @return A {@link CommandPart} that consists on a sequence of other CommandParts.
     */
    public static CommandPart sequential(String name, CommandPart... parts) {
        return new SequentialCommandPart(name, Arrays.asList(parts));
    }

    /**
     * A {@link CommandPart} that consist on a sequence of other CommandParts.
     * <p>
     * After calling {@link CommandPart#parse(CommandContext, ArgumentStack)} on this {@link CommandPart} every part on
     * this CommandPart will be parsed until all the parts are parsed or an error is thrown
     *
     * @param name  The name for this part.
     * @param parts The sequence of {@link CommandPart} instances that this {@link CommandPart} will delegate to.
     * @return A {@link CommandPart} that consists on a sequence of other CommandParts.
     */
    public static CommandPart sequential(String name, Collection<CommandPart> parts) {
        return new SequentialCommandPart(name, new ArrayList<>(parts));
    }

    /**
     * A {@link CommandPart} that takes an argument from the {@link ArgumentStack} and searches for a subcommand with that name,
     * after that the control is passed to the {@link SubCommandPart.SubCommandHandler} of the part
     * <p>
     * This method sets the optional value as false
     *
     * @param name     The name for this part.
     * @param commands The subcommands for this part.
     * @return A {@link CommandPart} that allows the usage of subcommands.
     */
    public static CommandPart subCommand(String name, Command... commands) {
        return new SubCommandPart(name, Arrays.asList(commands));
    }

    /**
     * A {@link CommandPart} that takes an argument from the {@link ArgumentStack} and searches for a subcommand with that name,
     * after that the control is passed to the {@link SubCommandPart.SubCommandHandler} of the part
     * <p>
     * This method sets the optional value as false
     *
     * @param name     The name for this part.
     * @param commands The subcommands for this part.
     * @return A {@link CommandPart} that allows the usage of subcommands.
     */
    public static CommandPart subCommand(String name, Collection<Command> commands) {
        return new SubCommandPart(name, new ArrayList<>(commands));
    }

    /**
     * A {@link CommandPart} that takes an argument from the {@link ArgumentStack} and searches for a subcommand with that name,
     * after that the control is passed to the {@link SubCommandPart.SubCommandHandler} of the part
     *
     * @param name     The name for this part.
     * @param commands The subcommands for this part.
     * @param optional If this subcommand should be Optional
     * @return A {@link CommandPart} that allows the usage of subcommands.
     */
    public static CommandPart subCommand(String name, Collection<Command> commands, boolean optional) {
        return new SubCommandPart(name, new ArrayList<>(commands), optional);
    }


    /**
     * A {@link CommandPart} that takes an argument from the {@link ArgumentStack} and searches for a subcommand with that name,
     * after that the control is passed to the {@link SubCommandPart.SubCommandHandler} of the part
     * <p>
     * This method sets the name of the part as "subcommand".
     * This method sets the optional value as false
     *
     * @param commands The subcommands for this part.
     * @return A {@link CommandPart} that allows the usage of subcommands.
     */
    public static CommandPart subCommand(Collection<Command> commands) {
        return new SubCommandPart("subcommand", new ArrayList<>(commands));
    }

    /**
     * A {@link CommandPart} that takes an argument from the {@link ArgumentStack} and searches for a subcommand with that name,
     * after that the control is passed to the {@link SubCommandPart.SubCommandHandler} of the part
     * <p>
     * This method sets the name of the part as "subcommand".
     * This method sets the optional value as false
     *
     * @param commands The subcommands for this part.
     * @return A {@link CommandPart} that allows the usage of subcommands.
     */
    public static CommandPart subCommand(Command... commands) {
        return new SubCommandPart("subcommand", Arrays.asList(commands));
    }

    /**
     * Creates a part that rewinds the {@link ArgumentStack} when it can't be parsed.
     * <p>
     * Do not use it with a {@link SubCommandPart} as parameter, instead use {@link Parts#subCommand(String, Collection, boolean)},
     * using this with a {@linkplain SubCommandPart} will cause lots of errors.
     *
     * @param part The {@link CommandPart} to make optional.
     * @return The given {@link CommandPart} that will rewind the {@link ArgumentStack} when it can't be parsed.
     */
    public static CommandPart optional(CommandPart part) {
        return new OptionalPart(part);
    }

    /**
     * Creates a part that rewinds the {@link ArgumentStack} and re-parses with default values when it can't be parsed the first time.
     * <p>
     * Do not use it with a {@link SubCommandPart} as parameter, instead use {@link Parts#subCommand(String, Collection, boolean)},
     * using this with a {@linkplain SubCommandPart} will cause lots of errors.
     *
     * @param part          The {@link CommandPart} to make optional.
     * @param defaultValues The list default values when the {@link ArgumentStack} can't be parsed
     * @return The given {@link CommandPart} that will rewind the {@link ArgumentStack} when it can't be parsed.
     */
    public static CommandPart optional(CommandPart part, List<String> defaultValues) {
        return new OptionalPart(part, defaultValues);
    }
}
