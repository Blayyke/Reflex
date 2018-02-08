package me.blayyke.reflex.utils;

import com.lambdaworks.redis.api.sync.RedisCommands;
import me.blayyke.reflex.database.DBEntryKey;
import me.blayyke.reflex.database.DBEntryKeyCCmd;
import net.dv8tion.jda.core.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

public class DatabaseUtils {
    private static Logger logger = LoggerFactory.getLogger("Reflex-Database");

    public static String getString(Guild guild, RedisCommands<String, String> sync, String key) {
        return sync.get(formatKey(guild, key));
    }

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

    public static void setString(Guild guild, RedisCommands<String, String> sync, String key, String value) {
        sync.set(formatKey(guild, key), value);
    }

    public static void setBoolean(Guild guild, RedisCommands<String, String> sync, DBEntryKey key, boolean value) {
        sync.set(formatKey(guild, key), String.valueOf(value));
    }

    public static void setNumber(Guild guild, RedisCommands<String, String> sync, DBEntryKey key, long value) {
        sync.set(formatKey(guild, key), String.valueOf(value));
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean exists(Guild guild, RedisCommands<String, String> sync, DBEntryKey key) {
        return sync.exists(formatKey(guild, key)) >= 1;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static String getHashString(Guild guild, RedisCommands<String, String> sync, DBEntryKey key, DBEntryKeyCCmd commandKey) {
        return getHashString(guild, sync, key.getRedisKey(), commandKey);
    }

    public static String getHashString(Guild guild, RedisCommands<String, String> sync, String key, DBEntryKeyCCmd commandKey) {
        return sync.hget(formatKey(guild, key), commandKey.getKey().toLowerCase());
    }

    public static void setHashString(Guild guild, RedisCommands<String, String> sync, DBEntryKey key, DBEntryKeyCCmd commandKey, String value) {
        setHashString(guild, sync, key.getRedisKey(), commandKey, value);
    }

    public static void setHashString(Guild guild, RedisCommands<String, String> sync, String key, DBEntryKeyCCmd field, String value) {
        sync.hset(formatKey(guild, key), field.getKey().toLowerCase(), value);
    }

    public static void setHashNumber(Guild guild, RedisCommands<String, String> sync, String key, DBEntryKeyCCmd field, long value) {
        setHashString(guild, sync, key, field, String.valueOf(value));
    }

    public static List<String> getHashKeys(Guild guild, RedisCommands<String, String> sync, DBEntryKey key) {
        return sync.hkeys(formatKey(guild, key));
    }

    public static void addToSet(Guild guild, RedisCommands<String, String> sync, DBEntryKey key, String value) {
        sync.sadd(formatKey(guild, key), value);
    }

    public static void setStringSet(Guild guild, RedisCommands<String, String> sync, DBEntryKey key, String... value) {
        delete(guild, sync, key);
        sync.sadd(formatKey(guild, key), value);
    }

    public static Set<String> getStringSet(Guild guild, RedisCommands<String, String> sync, DBEntryKey key) {
        return sync.smembers(formatKey(guild, key));
    }

    private static String formatKey(Guild guild, DBEntryKey key) {
        return formatKey(guild, key.getRedisKey());
    }

    private static String formatKey(Guild guild, String key) {
        return guild.getId() + "_" + key.toLowerCase();
    }

    public static void delete(Guild guild, RedisCommands<String, String> sync, String key) {
        sync.del(formatKey(guild, key));
    }

    public static void delete(Guild guild, RedisCommands<String, String> sync, DBEntryKey... keys) {
        String[] strKeys = new String[keys.length];
        for (int i = 0; i < keys.length; i++) strKeys[i] = formatKey(guild, keys[i]);

        sync.del(strKeys);
    }

    public static long getHashNumber(Guild guild, RedisCommands<String, String> sync, String s, DBEntryKeyCCmd creator) {
        return Long.parseLong(getHashString(guild, sync, s, creator));
    }

    public static void removeFromSet(Guild guild, RedisCommands<String, String> sync, DBEntryKey commands, String s) {
        sync.srem(formatKey(guild, commands), s);
    }
}