package me.fixeddev.commandflow.discord.part;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.discord.DiscordCommandManager;
import me.fixeddev.commandflow.discord.utils.ArgumentsUtils;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.kyori.text.TranslatableComponent;

import java.lang.reflect.Type;
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
    public List<? extends Member> parseValue(CommandContext context, ArgumentStack stack) throws ArgumentParseException {
        Message message = context.getObject(Message.class, DiscordCommandManager.MESSAGE_NAMESPACE);
        Guild guild = message.getTextChannel().getGuild();

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
            ArgumentParseException exception = new ArgumentParseException(TranslatableComponent.of("unknown.member"));
            exception.setArgument(this);

            throw exception;
        }

        return Collections.singletonList(member);
    }

    @Override
    public Type getType() {
        return Member.class;
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
