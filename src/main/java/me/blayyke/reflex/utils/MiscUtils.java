package me.blayyke.reflex.utils;

import me.blayyke.reflex.Reflex;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import okhttp3.Headers;
import okhttp3.Response;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Pattern;

import static me.blayyke.reflex.utils.ParseUtils.LONG_PATTERN;

public class MiscUtils {
    private static Random random = new Random();
    private static final Pattern DOUBLE_PATTERN = Pattern.compile("^[0-9]+\\.?[0-9]*$");

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

    public static String arrayToString(Enum[] args, String separator) {
        String[] strArgs = new String[args.length];
        for (int i = 0; i < args.length; i++) strArgs[i] = args[i].name().toLowerCase();

        return arrayToString(strArgs, separator);
    }

    public static String arrayToString(String[] args, String separator) {
        return arrayToString(args, 0, separator);
    }

    public static boolean isId(String query) {
        return query != null && LONG_PATTERN.matcher(query).matches();
    }

    public static boolean isDouble(String s) {
        return s != null && DOUBLE_PATTERN.matcher(s).matches();
    }

    public static String escapeFormatting(String prefix) {
        if (prefix == null) return null;
        return prefix
                .replace("`", "\\`")
                .replace("*", "\\*")
                .replace("_", "\\_");
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

    public static int getRandom(int i) {
        return random.nextInt(i);
    }

    public static boolean hasVoted(Reflex reflex, User user) {
        try {
            String url = "https://discordbots.org/api/bots/" + user.getJDA().getSelfUser().getId() + "/votes?onlyids=true";
            Headers headers = new Headers.Builder().set("Authorization", reflex.getDataManager().getSettings().getDboAuth()).build();
            Response sync = reflex.getHttpClient().getSync(url, headers);

            JSONArray array = new JSONArray(Objects.requireNonNull(sync.body()).string());
            List<String> voterList = new ArrayList<>();
            array.forEach(o -> voterList.add(o.toString()));

            sync.close();
            return voterList.contains(user.getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toHexString(String st) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < st.length(); i++) {
            builder.append(toHexString(st.charAt(i)));
        }
        return builder.toString();
    }

    public static String toHexString(char c) {
        StringBuilder hex = new StringBuilder(Integer.toHexString((int) c));
        while (hex.length() < 4) hex.insert(0, "0");
        return "\\u" + hex.toString();
    }

    public static String getCharName(String s) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            builder.append(Character.getName(s.charAt(i)));
        }
        return builder.toString();
    }

    @SafeVarargs
    public static <K> boolean equalsAny(K type, K... types) {
        for (K k : types)
            if (type != null && type.equals(k)) return true;
        return false;
    }

    public static String uppercaseFirst(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}