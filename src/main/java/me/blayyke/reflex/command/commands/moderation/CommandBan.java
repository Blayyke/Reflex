package me.blayyke.reflex.command.commands.moderation;

import me.blayyke.reflex.command.CommandPunishment;
import me.blayyke.reflex.utils.UserUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;

public class CommandBan extends CommandPunishment {
    @Override
    public Permission[] getBotRequiredPermissions() {
        return new Permission[]{Permission.BAN_MEMBERS};
    }

    @Override
    public Permission[] getRequiredPermissions() {
        return new Permission[]{Permission.BAN_MEMBERS};
    }

    @Override
    public EmbedBuilder applyPunishment(EmbedBuilder embed, Member member, Member requester) {
        member.getGuild().getController().kick(member, "Ban requested by " + UserUtils.formatUser(requester.getUser())).queue();

        embed.setTitle("Member banned");
        embed.setDescription("Successfully banned " + UserUtils.formatUser(member.getUser()));

        return embed;
    }

    @Override
    public String getName() {
        return "ban";
    }

    @Override
    public String getDesc() {
        return "Ban a member from the server";
    }
}