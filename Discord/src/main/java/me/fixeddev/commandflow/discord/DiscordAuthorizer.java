package me.fixeddev.commandflow.discord;

import me.fixeddev.commandflow.Authorizer;
import me.fixeddev.commandflow.Namespace;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;

public class DiscordAuthorizer implements Authorizer {

    @Override
    public boolean isAuthorized(Namespace namespace, String permission) {
        if (permission == null || permission.trim().isEmpty()) {
            return true;
        }

        Member member = namespace.getObject(Member.class, DiscordCommandManager.MEMBER_NAMESPACE);
        Message message = namespace.getObject(Message.class, DiscordCommandManager.MESSAGE_NAMESPACE);

        if (!message.isFromGuild()) {
            return true;
        }

        GuildChannel channel =  message.getGuildChannel();
        Permission permissionValue;

        try {
            permissionValue = Permission.valueOf(permission.toUpperCase());
        } catch (IllegalArgumentException e) {
            message.getChannel().sendMessage("Invalid permission: `" + permission + "`").queue();
            return false;
        }

        return member.hasPermission(channel, permissionValue);
    }
}
