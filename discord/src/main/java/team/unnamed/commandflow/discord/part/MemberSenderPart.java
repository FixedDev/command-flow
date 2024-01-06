package team.unnamed.commandflow.discord.part;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.discord.DiscordCommandManager;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.exception.CommandException;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.stack.ArgumentStack;
import net.dv8tion.jda.api.entities.Member;
import net.kyori.adventure.text.Component;

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

        throw new CommandException(Component.translatable("unknown.member"));
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
