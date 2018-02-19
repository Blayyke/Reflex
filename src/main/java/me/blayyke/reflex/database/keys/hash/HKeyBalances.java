package me.blayyke.reflex.database.keys.hash;

import net.dv8tion.jda.core.entities.User;

public class HKeyBalances extends AbstractHashKey {
    private final User user;

    public HKeyBalances(User user) {
        this.user = user;
    }

    @Override
    public String getFormattedKey() {
        return "balances";
    }

    @Override
    public String getField() {
        return user.getId();
    }
}