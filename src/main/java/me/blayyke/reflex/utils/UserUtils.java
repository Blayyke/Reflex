package me.blayyke.reflex.utils;

import net.dv8tion.jda.core.entities.User;

public class UserUtils {
    public static String formatUser(User user) {
        if (user == null) return null;
        return user.getName() + "#" + user.getDiscriminator();
    }
}