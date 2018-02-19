package me.blayyke.reflex.database;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;
import me.blayyke.reflex.Reflex;
import me.blayyke.reflex.database.keys.AbstractKey;
import me.blayyke.reflex.database.keys.guild.KeyCommands;
import me.blayyke.reflex.database.keys.hash.AbstractHashKey;
import me.blayyke.reflex.settings.DBSettings;
import net.dv8tion.jda.core.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class DBManager {
    private final Reflex reflex;
    private boolean initialized = false;
    private RedisClient redisClient;
    private StatefulRedisConnection<String, String> connection;
    private RedisCommands<String, String> sync;
    private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    public DBManager(Reflex bot) {
        this.reflex = bot;
    }

    public void init() {
        if (initialized)
            throw new IllegalStateException("Tried to initialize DBManager but it was already initialized!");

        DBSettings dbSettings = reflex.getDataManager().getSettings().getDatabaseSettings();

        RedisURI.Builder builder = RedisURI.builder()
                .withDatabase(dbSettings.getDatabaseNumber())
                .withHost(dbSettings.getDatabaseHost())
                .withPort(dbSettings.getDatabasePort());

        if (dbSettings.getDatabasePassword() != null && !dbSettings.getDatabasePassword().isEmpty())
            builder.withPassword(dbSettings.getDatabasePassword());

        redisClient = RedisClient.create(builder.build());
        connection = redisClient.connect();
        sync = connection.sync();
        sync.clientSetname("ReflexDBClient");

        getLogger().info("Successful DB startup.");
        initialized = true;
    }

    public void shutdown() {
        getLogger().info("Shutting down database connection.");
        connection.close();
        redisClient.shutdown();
    }

    public RedisCommands<String, String> getSync() {
        return sync;
    }

    public void loadGuild(Guild guild) {
        RedisCommands<String, String> sync = reflex.getDBManager().getSync();

        reflex.getCustomCommandManager().loadCommands(guild);
        reflex.getLogger().info("Finished setting up guild {} ({})", guild.getName(), guild.getId());
    }

    public void purgeGuild(Guild guild) {
        throw new NotImplementedException();
//        reflex.getLogger().info("Removed guild {} ({}) from database.", guild.getName(), guild.getId());
    }

    public Logger getLogger() {
        return logger;
    }


    public String get(AbstractKey key) {
        return getSync().get(key.getFormattedKey());
    }

    public void set(AbstractKey key, String value) {
        getSync().set(key.getFormattedKey(), value);
    }

    public boolean keyExists(AbstractKey key) {
        return getSync().exists(key.getFormattedKey()) >= 1;
    }

    public void setExpiry(AbstractKey key, TimeUnit timeUnit, int amount) {
        getSync().expire(key.getFormattedKey(), timeUnit.toSeconds(amount));
    }

    public Set<String> getSet(AbstractKey key) {
        return getSync().smembers(key.getFormattedKey());
    }

    public void appendToSet(AbstractKey key, String value) {
        getSync().sadd(key.getFormattedKey(), value);
    }

    public boolean hashExists(AbstractHashKey key) {
        return getSync().hexists(key.getFormattedKey(), key.getField());
    }

    public String hashGet(AbstractHashKey key) {
        return getSync().hget(key.getFormattedKey(), key.getField());
    }

    public void hashSet(AbstractHashKey key, String value) {
        getSync().hset(key.getFormattedKey(), key.getField(), value);
    }

    public void delete(AbstractKey key) {
        getSync().del(key.getFormattedKey());
    }

    public void removeFromSet(KeyCommands keyCommands, String name) {
        getSync().srem(keyCommands.getFormattedKey(), name);
    }
}