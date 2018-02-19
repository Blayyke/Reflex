package me.blayyke.reflex.listeners;

import com.lambdaworks.redis.api.sync.RedisCommands;
import me.blayyke.reflex.Reflex;
import me.blayyke.reflex.utils.MiscUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class JoinLeaveListener extends ListenerAdapter {
    private Reflex reflex;

    public JoinLeaveListener(Reflex reflex) {
        this.reflex = reflex;
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        onJoinLeave(event.getGuild(), event.getMember(), true);
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        onJoinLeave(event.getGuild(), event.getMember(), false);
    }

    private void onJoinLeave(Guild guild, Member member, boolean join) {
        RedisCommands<String, String> sync = reflex.getDBManager().getSync();

        Role autoRole = reflex.getDataManager().getGuildStorage(guild).getAutoRole();
        if ((autoRole) != null)
            guild.getController().addRolesToMember(member, autoRole).reason("Auto-role application.").queue();

        String message = join ? reflex.getDataManager().getGuildStorage(guild).getWelcomeMessage() : reflex.getDataManager().getGuildStorage(guild).getLeaveMessage();
        if (message == null || message.isEmpty()) return;

        TextChannel channel = reflex.getDataManager().getGuildStorage(guild).getAnnouncerChannel();
        if (channel == null) return;

        channel.sendMessage(MiscUtils.formatStringGuild(MiscUtils.formatStringUser(message, member.getUser()), guild)).queue();
    }
}