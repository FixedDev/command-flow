package me.fixeddev.commandflow.discord.part;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.discord.DiscordCommandManager;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.exception.CommandException;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
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
