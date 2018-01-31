package me.blayyke.reflex.utils;

import com.lambdaworks.redis.api.sync.RedisCommands;
import me.blayyke.reflex.database.DBEntryKey;
import net.dv8tion.jda.core.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseUtils {
    private static Logger logger = LoggerFactory.getLogger("Reflex-Database");

    public static String getString(Guild guild, RedisCommands<String, String> sync, DBEntryKey key) {
        return sync.get(formatKey(guild, key));
    }

    public static boolean getBoolean(Guild guild, RedisCommands<String, String> sync, DBEntryKey key) {
        return Boolean.parseBoolean(sync.get(formatKey(guild, key)));
    }

    public static long getNumber(Guild guild, RedisCommands<String, String> sync, DBEntryKey key) {
        return Long.parseLong(sync.get(formatKey(guild, key)));
    }

    public static void setString(Guild guild, RedisCommands<String, String> sync, DBEntryKey key, String value) {
        sync.set(formatKey(guild, key), value);
    }

    public static void setBoolean(Guild guild, RedisCommands<String, String> sync, DBEntryKey key, boolean value) {
        sync.set(formatKey(guild, key), String.valueOf(value));
    }

    public static void setNumber(Guild guild, RedisCommands<String, String> sync, DBEntryKey key, long value) {
        sync.set(formatKey(guild, key), String.valueOf(value));
    }

    public static boolean exists(Guild guild, RedisCommands<String, String> sync, DBEntryKey key) {
        return sync.exists(formatKey(guild, key)) >= 1;
    }

    private static String formatKey(Guild guild, DBEntryKey key) {
        return guild.getId() + "_" + key.getRedisKey().toLowerCase();
    }

    public static Logger getLogger() {
        return logger;
    }
}