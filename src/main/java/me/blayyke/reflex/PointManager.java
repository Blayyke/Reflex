package me.blayyke.reflex;

import me.blayyke.reflex.database.keys.hash.HKeyBalances;
import net.dv8tion.jda.core.entities.User;

public class PointManager {
    private final Reflex reflex;

    public PointManager(Reflex reflex) {
        this.reflex = reflex;
    }

    public long getPoints(User user) {
        if (!reflex.getDBManager().hashExists(new HKeyBalances(user)))
            reflex.getDBManager().hashSet(new HKeyBalances(user), String.valueOf(0));

        return Long.parseLong(reflex.getDBManager().hashGet(new HKeyBalances(user)));
    }

    public void addPoints(User user, long amount) {
        setPoints(user, getPoints(user) + amount);
    }

    public void takePoints(User user, long amount) {
        setPoints(user, getPoints(user) - amount);
    }

    public void setPoints(User user, long amount) {
        reflex.getDBManager().set(new HKeyBalances(user), String.valueOf(amount));
    }
}