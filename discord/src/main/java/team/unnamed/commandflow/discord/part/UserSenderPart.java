package team.unnamed.commandflow.discord.part;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.discord.DiscordCommandManager;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.exception.CommandException;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.stack.ArgumentStack;
import net.dv8tion.jda.api.entities.User;
import net.kyori.adventure.text.Component;

public class UserSenderPart implements CommandPart {

    private final String name;

    public UserSenderPart(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack, CommandPart parent) throws ArgumentParseException {
        User user = context.getObject(User.class, DiscordCommandManager.USER_NAMESPACE);

        if (user != null) {
            context.setValue(this, user);

            return;
        }

        throw new CommandException(Component.translatable("unknown.user"));
    }
}
