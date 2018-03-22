package me.blayyke.reflex.command.commands.moderation;

import me.blayyke.reflex.command.PunishmentCommand;
import me.blayyke.reflex.utils.UserUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;

public class CommandSoftBan extends PunishmentCommand {
    public CommandSoftBan() {
        super("softban", Permission.BAN_MEMBERS);
    }

    @Override
    public EmbedBuilder applyPunishment(EmbedBuilder embed, Member member, Member requester) {
        member.getGuild().getController().ban(member, 7).reason("Soft-ban requested by " + UserUtils.formatUser(requester.getUser())).queue(aVoid ->
                member.getGuild().getController().unban(member.getUser()).reason("Soft-ban requested by " + UserUtils.formatUser(requester.getUser())).queue()
        );

        embed.setTitle("Member soft-banned");
        embed.setDescription("Successfully soft-banned " + UserUtils.formatUser(member.getUser()));

        return embed;
    }

    @Override
    public int getRequiredArgs() {
        return 1;
    }
}