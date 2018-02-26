package me.blayyke.reflex.database.keys;

import me.blayyke.reflex.database.keys.guild.AbstractGuildKey;
import net.dv8tion.jda.core.entities.Guild;

public class KeyModLogChannel extends AbstractGuildKey {
    public KeyModLogChannel(Guild guild) {
        super(guild);
    }

    @Override
    public String getFormattedKey() {
        return getGuild().getId() + "_mlchannel";
    }
}