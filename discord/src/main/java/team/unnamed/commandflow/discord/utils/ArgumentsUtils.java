package team.unnamed.commandflow.discord.utils;

public final class ArgumentsUtils {

    public static boolean isValidSnowflake(String argument) {
        if (argument.length() != 18) {
            return false;
        }
        try {
            Long.parseLong(argument);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    public static boolean isValidTag(String argument) {
        String[] args = argument.split("#");
        if (args.length < 2) {
            return false;
        }
        return args[args.length - 1].length() == 4;
    }

    public static boolean isUserMention(String argument) {
        return isMention(argument, "@!");
    }

    public static boolean isChannelMention(String argument) {
        return isMention(argument, "#");
    }

    public static boolean isRoleMention(String argument) {
        return isMention(argument, "&");
    }

    private static boolean isMention(String argument, String mentionTypeIdentifier) {
        return argument.startsWith("<" + mentionTypeIdentifier) && argument.endsWith(">")
                && argument.length() >= (20 + mentionTypeIdentifier.length());
    }

}
