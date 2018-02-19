package me.blayyke.reflex.database.keys.hash.ccmd;

import net.dv8tion.jda.core.entities.Guild;

public class CCFieldAction extends AbstractCCHashField {
    public CCFieldAction(Guild guild, String name) {
        super(guild, name);
    }

    @Override
    public String getField() {
        return "action";
    }
}