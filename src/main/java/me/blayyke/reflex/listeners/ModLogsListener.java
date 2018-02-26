package me.blayyke.reflex.listeners;

import me.blayyke.reflex.Reflex;
import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.utils.MiscUtils;
import me.blayyke.reflex.utils.UserUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.audit.ActionType;
import net.dv8tion.jda.core.audit.AuditLogEntry;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ModLogsListener extends ListenerAdapter {
    private Reflex reflex;

    public ModLogsListener(Reflex reflex) {
        this.reflex = reflex;
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        TextChannel channel = reflex.getDataManager().getGuildStorage(event.getGuild()).getModLogChannel();
        if (!event.getGuild().getSelfMember().hasPermission(Permission.VIEW_AUDIT_LOGS)) return;
        if (channel == null) return;

        event.getGuild().getAuditLogs().queue(entries -> {
            AuditLogEntry entry = entries.get(0);
            if (!MiscUtils.equalsAny(entry.getType(), ActionType.BAN, ActionType.KICK, ActionType.UNBAN)) return;

            User user = reflex.getShardManager().getUserById(entry.getTargetIdLong());
            EmbedBuilder embedBuilder = AbstractCommand.createEmbed();

            embedBuilder.setTitle("Member " + entry.getType().toString().toLowerCase());

            embedBuilder.addField("User", UserUtils.formatUser(user), true);
            embedBuilder.addBlankField(true);
            embedBuilder.addField("Staff Member", UserUtils.formatUser(entry.getUser()), true);

            embedBuilder.addField("Reason", entry.getReason() == null ? "No reason set." : entry.getReason(), false);

            channel.sendMessage(embedBuilder.build()).queue();
        });
    }
}