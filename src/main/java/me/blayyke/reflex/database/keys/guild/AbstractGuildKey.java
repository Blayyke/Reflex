package me.blayyke.reflex.database.keys.guild;

import me.blayyke.reflex.database.keys.AbstractKey;
import net.dv8tion.jda.core.entities.Guild;

public abstract class AbstractGuildKey extends AbstractKey {
    private Guild guild;

    public AbstractGuildKey(Guild guild) {
        this.guild = guild;
    }

    public Guild getGuild() {
        return guild;
    }
}