package me.blayyke.reflex;

import me.blayyke.reflex.settings.BotSettings;
import me.blayyke.reflex.utils.Version;
import net.dv8tion.jda.core.entities.Guild;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DataManager {
    private final Reflex reflex;
    private BotSettings settings;

    private Map<Long, GuildStorage> guildStorageMap = new HashMap<>();

    public DataManager(Reflex reflex) {
        this.reflex = reflex;
    }

    public void init() throws IOException {
        settings = new BotSettings(reflex);
        settings.load();

        Version.loadVersion();
    }

    public void setupGuildStorage(Guild guild) {
        guildStorageMap.put(guild.getIdLong(), new GuildStorage(reflex, guild));
    }

    public GuildStorage getGuildStorage(Guild guild) {
        if (!guildStorageMap.containsKey(guild.getIdLong())) setupGuildStorage(guild);

        return guildStorageMap.get(guild.getIdLong());
    }

    public BotSettings getSettings() {
        return settings;
    }
}