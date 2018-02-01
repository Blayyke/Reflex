package me.blayyke.reflex.listeners;

import com.lambdaworks.redis.api.sync.RedisCommands;
import me.blayyke.reflex.Reflex;
import me.blayyke.reflex.database.DBEntryKey;
import me.blayyke.reflex.utils.DatabaseUtils;
import me.blayyke.reflex.utils.MiscUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
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
        sendJoinLeaveMessage(event.getGuild(), event.getMember(), true);
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        sendJoinLeaveMessage(event.getGuild(), event.getMember(), false);
    }

    private void sendJoinLeaveMessage(Guild guild, Member member, boolean join) {
        RedisCommands<String, String> sync = reflex.getDBManager().getSync();

        String message = DatabaseUtils.getString(guild, sync, join ? DBEntryKey.JOIN_MESSAGE : DBEntryKey.LEAVE_MESSAGE);
        long channelId = DatabaseUtils.getNumber(guild, sync, DBEntryKey.ANNOUNCEMENT_CHANNEL);

        if (message == null || message.isEmpty()) return;
        TextChannel channel = guild.getTextChannelById(channelId);
        if (channel == null) return;

        channel.sendMessage(
                MiscUtils.formatStringGuild(
                        MiscUtils.formatStringUser(message, member.getUser()
                        ), guild)
        ).queue();
    }
}