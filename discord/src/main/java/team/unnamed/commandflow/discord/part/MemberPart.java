package team.unnamed.commandflow.discord.part;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.discord.DiscordCommandManager;
import team.unnamed.commandflow.discord.utils.ArgumentsUtils;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.part.ArgumentPart;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.stack.ArgumentStack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.kyori.adventure.text.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MemberPart implements ArgumentPart {

    private final String name;

    public MemberPart(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<? extends Member> parseValue(CommandContext context, ArgumentStack stack, CommandPart parent) throws ArgumentParseException {
        Message message = context.getObject(Message.class, DiscordCommandManager.MESSAGE_NAMESPACE);
        Guild guild = message.getGuild();

        String target = stack.next();

        Member member = null;

        if (ArgumentsUtils.isValidSnowflake(target)) {
            member = guild.getMemberById(target);
        }

        if (member == null && ArgumentsUtils.isValidTag(target)) {
            member = guild.getMemberByTag(target);
        }

        if (member == null && ArgumentsUtils.isUserMention(target)) {
            String id = target.substring(3, target.length() - 1);
            member = guild.getMemberById(id);
        }

        if (member == null) {
            throw new ArgumentParseException(Component.translatable("unknown.member"))
                    .setArgument(this);
        }

        return Collections.singletonList(member);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessagePart)) return false;
        MemberPart that = (MemberPart) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
