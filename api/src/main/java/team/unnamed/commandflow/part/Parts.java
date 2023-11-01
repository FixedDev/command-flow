package team.unnamed.commandflow.part;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.command.Command;
import team.unnamed.commandflow.part.defaults.BooleanPart;
import team.unnamed.commandflow.part.defaults.DoublePart;
import team.unnamed.commandflow.part.defaults.EnumPart;
import team.unnamed.commandflow.part.defaults.FirstMatchPart;
import team.unnamed.commandflow.part.defaults.LongPart;
import team.unnamed.commandflow.part.defaults.SwitchPart;
import team.unnamed.commandflow.part.defaults.FloatPart;
import team.unnamed.commandflow.part.defaults.IntegerPart;
import team.unnamed.commandflow.part.defaults.LimitingPart;
import team.unnamed.commandflow.part.defaults.OptionalPart;
import team.unnamed.commandflow.part.defaults.SequentialCommandPart;
import team.unnamed.commandflow.part.defaults.StringPart;
import team.unnamed.commandflow.part.defaults.SubCommandPart;
import team.unnamed.commandflow.part.defaults.ValueFlagPart;
import team.unnamed.commandflow.stack.ArgumentStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * A utility class to ease the use of {@link CommandPart} and its sub-classes
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
     * Creates a new {@link SwitchPart} that searches non-positional boolean arguments with the next format
     * -&lt;shortName&gt;, the value for this part will be true if the argument is present at least one time,
     * false if it isn't.
     *
     * <p>After the argument being found it will be deleted from the {@link ArgumentStack} so it doesn't interfere
     * with other parsed {@link CommandPart}</p>
     *
     * @param shortName The short name of the {@link SwitchPart}, this will be used as the flag name(-&lt;shortName&gt;)
     * @return A {@link CommandPart} that will detect flags on the argument stack and delete them, returning true or false depending on whether it's present or not.
     */
    public static CommandPart switchPart(String shortName) {
        return switchPart(shortName, shortName);
    }

    /**
     * Creates a new {@link SwitchPart} that searches non-positional boolean arguments with the next format
     * -&lt;shortName&gt;, the value for this part will be true if the argument is present at least one time,
     * false if it isn't.
     *
     * <p>After the argument being found it will be deleted from the {@link ArgumentStack} so it doesn't interfere
     * with other parsed {@link CommandPart}</p>
     *
     * @param name      The full internal name of the {@link SwitchPart}.
     * @param shortName The short name of the {@link SwitchPart}, this will be used as the flag name(-&lt;shortName&gt;)
     * @return A {@link CommandPart} that will detect flags on the argument stack and delete them, returning true or false depending on whether it's present or not.
     */
    public static CommandPart switchPart(String name, String shortName) {
        return switchPart(name, shortName, false);
    }

    /**
     * Creates a new {@link SwitchPart} that searches non-positional boolean arguments with the format
     * -&lt;shortName&gt; or --&lt;name&gt;, the second one only being allowed if the allowFullNameUse
     * parameter is true; the value for this part will be true if the argument is present at least one
     * time in any of its formats, false if it isn't.
     *
     * <p>After the argument being found it will be deleted from the {@link ArgumentStack} so it doesn't
     * interfere with other parsed {@link CommandPart}</p>
     *
     * @param name             The full name of the {@link SwitchPart}, if the parameter allowFullNameUse is true, this will be allowed as flag name(--&lt;name&gt;).
     * @param shortName        The short name of the {@link SwitchPart}, this will be used as the flag name(-&lt;shortName&gt;)
     * @param allowFullNameUse Whether the format --&lt;name&gt; is allowed or not.
     * @return A {@link CommandPart} that will detect flags on the argument stack and delete them, returning true or false depending on whether it's present or not.
     */
    public static CommandPart switchPart(String name, String shortName, boolean allowFullNameUse) {
        return new SwitchPart(name, shortName, allowFullNameUse);
    }

    /**
     * Creates a new {@link ValueFlagPart} that searches non-positional arguments with the format
     * -&lt;shortName&gt; the value for this part will be the value returned by the provided {@link CommandPart}.
     *
     * <p>After the argument being found it will be deleted from the {@link ArgumentStack} and in that position
     * of the stack the provided {@link CommandPart} will start the parsing, if the parse fails the whole process
     * is reverted. The parsed arguments are removed also</p>
     *
     * @param part      The {@link CommandPart} that will parse the arguments.
     * @param shortName The short name of the {@link SwitchPart}, this will be used as the flag name(-&lt;shortName&gt;)
     * @return A {@link CommandPart} that will detect flags on the argument stack and delete them, returning true or false depending on whether it's present or not.
     * @see Parts#switchPart(String, String, boolean)
     */
    public static CommandPart valueFlag(CommandPart part, String shortName) {
        return valueFlag(part, shortName, false);
    }

    /**
     * Creates a new {@link ValueFlagPart} that searches non-positional arguments with the format
     * -&lt;shortName&gt; or --&lt;name&gt;(name being the name of the provided part), the second
     * one only being allowed if the allowFullNameUse parameter is true; the value for this part
     * will be the value returned by the provided {@link CommandPart}.
     *
     * <p>After the argument being found it will be deleted from the {@link ArgumentStack} and in
     * that position of the stack the provided {@link CommandPart} will start the parsing, if the
     * parse fails the whole process is reverted. The parsed arguments are also removed</p>
     *
     * @param part             The {@link CommandPart} that will parse the arguments.
     * @param shortName        The short name of the {@link SwitchPart}, this will be used as the flag name(-&lt;shortName&gt;)
     * @param allowFullNameUse Whether the format --&lt;name&gt; is allowed or not.
     * @return A {@link CommandPart} that will detect flags on the argument stack and delete them, returning true or false depending on whether it's present or not.
     * @see Parts#switchPart(String, String, boolean)
     */
    public static CommandPart valueFlag(CommandPart part, String shortName, boolean allowFullNameUse) {
        return new ValueFlagPart(shortName, allowFullNameUse, part);
    }


    /**
     * A basic {@link CommandPart} that takes a string from the {@link ArgumentStack}
     * and converts it into an Enum value.
     *
     * @param name     The name for this part.
     * @param enumType The type of the enum for this part.
     * @return A {@link CommandPart} with the given name that takes a {@link Enum} as argument.
     */
    public static CommandPart enumPart(String name, Class<? extends Enum<?>> enumType) {
        return new EnumPart(name, enumType);
    }


    /**
     * A basic {@link CommandPart} that takes a string from the {@link ArgumentStack}
     *
     * @param name The name for this part.
     * @return A {@link CommandPart} with the given name that takes a {@link String} as argument.
     */
    public static CommandPart string(String name) {
        return new StringPart(name);
    }

    /**
     * Returns a non-ranged {@link LongPart} with the
     * given {@code name}.
     *
     * @param name The name for the part.
     * @return A {@link LongPart} with the provided {@code name}.
     */
    public static CommandPart longPart(String name) {
        return new LongPart(name);
    }

    /**
     * Returns a ranged {@link LongPart}, with the given
     * {@code name}, {@code min} and {@code max} range.
     *
     * @param name The name for the part.
     * @param min The minimum range for the part.
     * @param max The maximum range for the part.
     * @return A {@link LongPart} with the given {@code name}, {@code min} and {@code max} range.
     */
    public static CommandPart longPart(String name, long min, long max) {
        return new LongPart(name, min, max);
    }

    /**
     * A basic {@link CommandPart} that takes a string from the {@link ArgumentStack}
     *
     * @param name The name for this part.
     * @return A {@link CommandPart} with the given name that takes a {@link Integer} as argument.
     */
    public static CommandPart integer(String name) {
        return new IntegerPart(name);
    }

    /**
     * A basic {@link CommandPart} that takes a string from the {@link ArgumentStack}
     *
     * @param name The name for this part.
     * @param max  The maximum number allowed by this part.
     * @param min  The minimum number allowed by this part.
     * @return A {@link CommandPart} with the given name that takes a {@link Integer} as argument.
     */
    public static CommandPart integer(String name, int min, int max) {
        return new IntegerPart(name, min, max);
    }

    /**
     * A basic {@link CommandPart} that takes a string from the {@link ArgumentStack}
     *
     * @param name The name for this part.
     * @return A {@link CommandPart} with the given name that takes a {@link Double} as argument.
     */
    public static CommandPart doublePart(String name) {
        return new DoublePart(name);
    }

    /**
     * A basic {@link CommandPart} that takes a string from the {@link ArgumentStack}
     *
     * @param name The name for this part.
     * @param max  The maximum number allowed by this part.
     * @param min  The minimum number allowed by this part.
     * @return A {@link CommandPart} with the given name that takes a {@link Double} as argument.
     */
    public static CommandPart doublePart(String name, double min, double max) {
        return new DoublePart(name, min, max);
    }

    /**
     * A basic {@link CommandPart} that takes a string from the {@link ArgumentStack}
     *
     * @param name The name for this part.
     * @return A {@link CommandPart} with the given name that takes a {@link Float} as argument.
     */
    public static CommandPart floatPart(String name) {
        return new FloatPart(name);
    }


    /**
     * A basic {@link CommandPart} that takes a string from the {@link ArgumentStack}
     *
     * @param name The name for this part.
     * @param max  The maximum number allowed by this part.
     * @param min  The minimum number allowed by this part.
     * @return A {@link CommandPart} with the given name that takes a {@link Float} as argument.
     */
    public static CommandPart floatPart(String name, float min, float max) {
        return new FloatPart(name, min, max);
    }


    /**
     * A basic {@link CommandPart} that takes a string from the {@link ArgumentStack}
     *
     * @param name The name for this part.
     * @return A {@link CommandPart} with the given name that takes a {@link Boolean} as argument.
     */
    public static CommandPart booleanPart(String name) {
        return new BooleanPart(name);
    }

    /**
     * A {@link CommandPart} that consist on a sequence of other CommandParts.
     * <p>
     * After calling {@link CommandPart#parse(CommandContext, ArgumentStack, CommandPart)} on this {@link CommandPart} every part on
     * this {@link FirstMatchPart} will be parsed until some part parses correctly.
     *
     * @param name  The name for this part.
     * @param parts The sequence of {@link CommandPart} instances that this {@link CommandPart} will delegate to.
     * @return A {@link CommandPart} that consists on a sequence of other CommandParts, using the first one that parsed correctly.
     */
    public static CommandPart firstMatch(String name, CommandPart... parts) {
        return new FirstMatchPart(name, Arrays.asList(parts));
    }

    /**
     * A {@link CommandPart} that consist on a sequence of other CommandParts.
     * <p>
     * After calling {@link CommandPart#parse(CommandContext, ArgumentStack, CommandPart)} on this {@link CommandPart} every part on
     * this {@link FirstMatchPart} will be parsed until some part parses correctly/
     *
     * @param name  The name for this part.
     * @param parts The sequence of {@link CommandPart} instances that this {@link CommandPart} will delegate to.
     * @return A {@link CommandPart} that consists on a sequence of other CommandParts, using the first one that parsed correctly.
     */
    public static CommandPart firstMatch(String name, Collection<CommandPart> parts) {
        return new FirstMatchPart(name, new ArrayList<>(parts));
    }

    /**
     * A {@link CommandPart} that consist on a sequence of other CommandParts.
     * <p>
     * After calling {@link CommandPart#parse(CommandContext, ArgumentStack, CommandPart)} on this {@link CommandPart} every part on
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
     * After calling {@link CommandPart#parse(CommandContext, ArgumentStack, CommandPart)} on this {@link CommandPart} every part on
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
     * Creates a part that rewinds the {@link ArgumentStack} and re-parses with default values when it can't be parsed.
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

    /**
     * Creates a part that rewinds the {@link ArgumentStack} when there's not an argument present.
     * <p>
     * This will throw the errors if the parsing fails and is the last part to parse
     * <p>
     * Do not use it with a {@link SubCommandPart} as parameter, instead use {@link Parts#subCommand(String, Collection, boolean)},
     * using this with a {@linkplain SubCommandPart} will cause lots of errors.
     *
     * @param part The {@link CommandPart} to make optional.
     * @return The given {@link CommandPart} that will rewind the {@link ArgumentStack} when it can't be parsed.
     */
    public static CommandPart strictOptional(CommandPart part) {
        return new OptionalPart(part, false);
    }

    /**
     * Creates a part that rewinds the {@link ArgumentStack} and re-parses with default values when there's not an argument present.
     * <p>
     * This will throw the errors if the parsing fails and is the last part to parse
     * <p>
     * Do not use it with a {@link SubCommandPart} as parameter, instead use {@link Parts#subCommand(String, Collection, boolean)},
     * using this with a {@linkplain SubCommandPart} will cause lots of errors.
     *
     * @param part          The {@link CommandPart} to make optional.
     * @param defaultValues The list default values when the {@link ArgumentStack} can't be parsed
     * @return The given {@link CommandPart} that will rewind the {@link ArgumentStack} when it can't be parsed.
     */
    public static CommandPart strictOptional(CommandPart part, List<String> defaultValues) {
        return new OptionalPart(part, false, defaultValues);
    }

}
