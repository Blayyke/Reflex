package me.blayyke.reflex.database.keys.user;

import net.dv8tion.jda.core.entities.User;

public class KeyPointsCooldown extends AbstractUserKey {
    public KeyPointsCooldown(User user) {
        super(user);
    }

    @Override
    public String getFormattedKey() {
        return "points_cd_" + getUser().getId();
    }
}