package team.unnamed.commandflow.discord;

import team.unnamed.commandflow.Authorizer;
import team.unnamed.commandflow.Namespace;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

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

        GuildChannel channel =  message.getTextChannel();
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
