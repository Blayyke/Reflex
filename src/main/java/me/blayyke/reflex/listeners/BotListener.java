package me.blayyke.reflex.listeners;

import me.blayyke.reflex.Reflex;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
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

        event.getJDA().getGuilds().forEach(reflex.getDBManager()::loadGuild);
    }
}