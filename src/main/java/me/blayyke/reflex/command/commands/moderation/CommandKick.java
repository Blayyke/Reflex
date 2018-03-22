package me.blayyke.reflex.command.commands.moderation;

import me.blayyke.reflex.command.PunishmentCommand;
import me.blayyke.reflex.utils.UserUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;

public class CommandKick extends PunishmentCommand {
    public CommandKick() {
        super("kick", Permission.KICK_MEMBERS);
    }

    @Override
    public EmbedBuilder applyPunishment(EmbedBuilder embed, Member member, Member requester) {
        member.getGuild().getController().kick(member, "Kick requested by " + UserUtils.formatUser(requester.getUser())).queue();

        embed.setTitle("Member kicked");
        embed.setDescription("Successfully kicked " + UserUtils.formatUser(member.getUser()));

        return embed;
    }

    @Override
    public int getRequiredArgs() {
        return 1;
    }
}