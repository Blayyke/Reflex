package me.blayyke.reflex.database.keys;

import net.dv8tion.jda.core.entities.Guild;

public class KeyCustomCommand extends AbstractKey {
    private final Guild guild;
    private final String name;

    public KeyCustomCommand(Guild guild, String name) {
        this.guild = guild;
        this.name = name;
    }

    @Override
    public String getFormattedKey() {
        return getGuild().getId() + "_cc_" + getName();
    }

    public Guild getGuild() {
        return guild;
    }

    public String getName() {
        return name;
    }
}