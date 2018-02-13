package me.blayyke.reflex.listeners;

import me.blayyke.reflex.Reflex;
import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.utils.UserUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.time.OffsetDateTime;

public class GuildListener extends ListenerAdapter {
    private final Reflex reflex;

    public GuildListener(Reflex reflex) {
        this.reflex = reflex;
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        onGuildUpdate(event.getGuild(), true);

        reflex.getDBManager().loadGuild(event.getGuild());
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        onGuildUpdate(event.getGuild(), false);

        reflex.getDBManager().purgeGuild(event.getGuild());
    }

    private void onGuildUpdate(Guild guild, boolean join) {
        if (!guild.getSelfMember().getJoinDate().isAfter(OffsetDateTime.now().minusMinutes(10))) {
            reflex.getLogger().warn("Got a guild join for a guild we joined more than 10 minutes ago! Discord outage?");
            return;
        }

        reflex.getStatsPoster().updateAllStats(guild.getJDA());

        EmbedBuilder embed = AbstractCommand.createEmbed();
        embed.setTitle((join ? "Joined" : "Left") + " guild");
        embed.setDescription((join ? "Joined" : "Left") + " guild " + guild.getName() + ".");
        embed.addField("Guild ID", guild.getId(), false);
        embed.addField("Guild Owner", UserUtils.formatUser(guild.getOwner().getUser()), false);

        int bots = (int) guild.getMembers().stream().filter(m -> m.getUser().isBot()).count();
        int users = guild.getMembers().size() - bots;
        embed.addField("Members", bots + " bots, " + users + " users.", false);

        TextChannel channel = reflex.getShardManager().getTextChannelById(407492928580354058L);

        if (!channel.canTalk()) {
            reflex.getLogger().warn("Cannot talk in guild log channel " + channel.getName() + "!");
            return;
        }

        channel.sendMessage(embed.build()).queue();
    }
}