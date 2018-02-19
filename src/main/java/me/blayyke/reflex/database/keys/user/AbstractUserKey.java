package me.blayyke.reflex.database.keys.user;

import me.blayyke.reflex.database.keys.AbstractKey;
import net.dv8tion.jda.core.entities.User;

public abstract class AbstractUserKey extends AbstractKey {
    private User user;

    public AbstractUserKey(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}