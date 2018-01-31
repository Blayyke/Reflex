package me.blayyke.reflex.database;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;
import me.blayyke.reflex.Reflex;
import me.blayyke.reflex.settings.DBSettings;
import me.blayyke.reflex.utils.DatabaseUtils;

public class DBManager {
    private final Reflex bot;
    private boolean initialized = false;
    private RedisClient redisClient;
    private StatefulRedisConnection<String, String> connection;
    private RedisCommands<String, String> sync;

    public DBManager(Reflex bot) {
        this.bot = bot;
    }

    public void init() {
        if (initialized)
            throw new IllegalStateException("Tried to initialize DBManager but it was already initialized!");

        DBSettings dbSettings = bot.getSettings().getDatabaseSettings();

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

        DatabaseUtils.getLogger().info("Successful DB startup.");
        initialized = true;
    }

    public void shutdown() {
        DatabaseUtils.getLogger().info("Shutting down database connection.");
        connection.close();
        redisClient.shutdown();
    }

    public RedisCommands<String, String> getSync() {
        return sync;
    }
}