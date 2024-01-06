package team.unnamed.commandflow.discord.factory;

import team.unnamed.commandflow.annotated.part.AbstractModule;
import team.unnamed.commandflow.annotated.part.Key;
import team.unnamed.commandflow.annotated.annotation.Sender;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class DiscordModule extends AbstractModule {

    @Override
    public void configure() {
        bindFactory(Member.class, new MemberPartFactory());
        bindFactory(User.class, new UserPartFactory());
        bindFactory(Message.class, new MessagePartFactory());
        bindFactory(TextChannel.class, new TextChannelPartFactory());
        bindFactory(new Key(Member.class, Sender.class), new MemberSenderPartFactory());
        bindFactory(new Key(User.class, Sender.class), new UserSenderPartFactory());
    }
}
