package me.blayyke.reflex.database.keys.hash.ccmd;

import me.blayyke.reflex.database.keys.hash.AbstractHashKey;
import net.dv8tion.jda.core.entities.Guild;

public abstract class AbstractCCHashField extends AbstractHashKey {
    private final String name;
    private String formattedKey;
    private Guild guild;

    public AbstractCCHashField(Guild guild, String name) {
        this.guild = guild;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Guild getGuild() {
        return guild;
    }

    @Override
    public String getFormattedKey() {
        return getGuild().getId() + "_" + getName();
    }
}