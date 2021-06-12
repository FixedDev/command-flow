package me.fixeddev.commandflow.discord.part;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.discord.DiscordCommandManager;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.exception.CommandException;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import net.dv8tion.jda.api.entities.Member;
import net.kyori.text.TranslatableComponent;

import java.util.Objects;

public class MemberSenderPart implements CommandPart {

    private final String name;

    public MemberSenderPart(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack, CommandPart parent) throws ArgumentParseException {
        Member member = context.getObject(Member.class, DiscordCommandManager.MEMBER_NAMESPACE);

        if (member != null) {
            context.setValue(this, member);
            return;
        }

        throw new CommandException(TranslatableComponent.of("unknown.member"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemberSenderPart)) return false;
        MemberSenderPart that = (MemberSenderPart) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
