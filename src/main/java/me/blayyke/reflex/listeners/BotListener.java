package me.blayyke.reflex.listeners;

import com.lambdaworks.redis.api.sync.RedisCommands;
import me.blayyke.reflex.Reflex;
import me.blayyke.reflex.database.DBEntryKey;
import me.blayyke.reflex.utils.DatabaseUtils;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class BotListener extends ListenerAdapter {
    private Reflex reflex;

    public BotListener(Reflex reflex) {
        this.reflex = reflex;
    }

    @Override
    public void onReady(ReadyEvent event) {
        reflex.getLogger().info("Shard {} ready.", event.getJDA().getShardInfo().getShardString());
        reflex.setDeveloperId(event.getJDA().asBot().getApplicationInfo().complete().getOwner().getIdLong());
        event.getJDA().getPresence().setPresence(OnlineStatus.ONLINE, Game.playing(reflex.getSettings().getGameName()));

        for (Guild guild : event.getJDA().getGuilds()) {
            // String
            RedisCommands<String, String> sync = reflex.getDBManager().getSync();
            if (!DatabaseUtils.exists(guild, sync, DBEntryKey.GUILD_PREFIX))
                DatabaseUtils.setString(guild, sync, DBEntryKey.GUILD_PREFIX, reflex.getSettings().getDefaultPrefix());
            if (!DatabaseUtils.exists(guild, sync, DBEntryKey.JOIN_MESSAGE))
                DatabaseUtils.setString(guild, sync, DBEntryKey.JOIN_MESSAGE, null);
            if (!DatabaseUtils.exists(guild, sync, DBEntryKey.LEAVE_MESSAGE))
                DatabaseUtils.setString(guild, sync, DBEntryKey.LEAVE_MESSAGE, null);

            // Number
            if (!DatabaseUtils.exists(guild, sync, DBEntryKey.ANNOUNCEMENT_CHANNEL))
                DatabaseUtils.setNumber(guild, sync, DBEntryKey.ANNOUNCEMENT_CHANNEL, -1);

            reflex.getLogger().info("Setup guild {} ({})", guild.getName(), guild.getId());
        }
    }
}