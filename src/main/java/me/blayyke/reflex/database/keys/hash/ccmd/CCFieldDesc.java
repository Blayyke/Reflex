package me.blayyke.reflex.database.keys.hash.ccmd;

import net.dv8tion.jda.core.entities.Guild;

public class CCFieldDesc extends AbstractCCHashField {
    public CCFieldDesc(Guild guild, String name) {
        super(guild, name);
    }

    @Override
    public String getField() {
        return "desc";
    }
}