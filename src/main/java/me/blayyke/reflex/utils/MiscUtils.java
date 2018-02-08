package me.blayyke.reflex.utils;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;

import static me.blayyke.reflex.utils.ParseUtils.LONG_PATTERN;

public class MiscUtils {
    public static String arrayToString(String[] args, int startIndex, String separator) {
        StringBuilder builder = new StringBuilder();
        if (args.length == 0) return "";
        if (args.length <= startIndex) startIndex = 0;

        for (int i = startIndex; ; i++) {
            builder.append(args[i]);
            if (i == args.length - 1) break;
            builder.append(separator);
        }

        return builder.toString();
    }

    public static boolean isAllLetters(CharSequence charSequence) {
        return charSequence.chars().allMatch(Character::isLetter);
    }

    public static String arrayToString(String[] args, String separator) {
        return arrayToString(args, 0, separator);
    }

    public static boolean isId(String query) {
        return query != null && LONG_PATTERN.matcher(query).matches();
    }

    public static String escapeFormatting(String prefix) {
        if (prefix == null) return null;
        return prefix
                .replace("`", "\\`")
                .replace("*", "\\*")
                .replace("_", "\\_");
    }

    public static boolean equalsAny(String message, String... toCheck) {
        for (String s : toCheck) if (s != null && s.equals(message)) return true;
        return false;
    }

    public static String formatStringUser(String string, User user) {
        return string
                .replace("%user.name", user.getName())
                .replace("%user.id", user.getId())
                .replace("%user.mention", user.getAsMention())
                .replace("%user", user.getName() + "#" + user.getDiscriminator());
    }

    public static String formatStringGuild(String string, Guild guild) {
        return string
                .replace("%server.name", guild.getName())
                .replace("%guild.name", guild.getName())
                .replace("%server.id", guild.getId())
                .replace("%guild.id", guild.getId());
    }
}