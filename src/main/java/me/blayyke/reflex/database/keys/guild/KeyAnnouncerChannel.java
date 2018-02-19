package me.blayyke.reflex.database.keys.guild;

import net.dv8tion.jda.core.entities.Guild;

public class KeyAnnouncerChannel extends AbstractGuildKey {
    public KeyAnnouncerChannel(Guild guild) {
        super(guild);
    }

    @Override
    public String getFormattedKey() {
        return getGuild().getId() + "_achannel";
    }
}