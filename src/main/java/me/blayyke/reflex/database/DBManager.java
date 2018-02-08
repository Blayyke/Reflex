package me.blayyke.reflex.database;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;
import me.blayyke.reflex.Reflex;
import me.blayyke.reflex.settings.DBSettings;
import me.blayyke.reflex.utils.DatabaseUtils;
import net.dv8tion.jda.core.entities.Guild;

public class DBManager {
    private final Reflex reflex;
    private boolean initialized = false;
    private RedisClient redisClient;
    private StatefulRedisConnection<String, String> connection;
    private RedisCommands<String, String> sync;

    public DBManager(Reflex bot) {
        this.reflex = bot;
    }

    public void init() {
        if (initialized)
            throw new IllegalStateException("Tried to initialize DBManager but it was already initialized!");

        DBSettings dbSettings = reflex.getSettings().getDatabaseSettings();

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

    public void loadGuild(Guild guild) {
        // String
        RedisCommands<String, String> sync = reflex.getDBManager().getSync();
        if (!DatabaseUtils.exists(guild, sync, DBEntryKey.GUILD_PREFIX))
            DatabaseUtils.setString(guild, sync, DBEntryKey.GUILD_PREFIX, reflex.getSettings().getDefaultPrefix());
        if (!DatabaseUtils.exists(guild, sync, DBEntryKey.JOIN_MESSAGE))
            DatabaseUtils.setString(guild, sync, DBEntryKey.JOIN_MESSAGE, null);
        if (!DatabaseUtils.exists(guild, sync, DBEntryKey.LEAVE_MESSAGE))
            DatabaseUtils.setString(guild, sync, DBEntryKey.LEAVE_MESSAGE, null);

        // Number
        if (!DatabaseUtils.exists(guild, sync, DBEntryKey.ANNOUNCEMENT_CHANNEL))
            DatabaseUtils.setNumber(guild, sync, DBEntryKey.ANNOUNCEMENT_CHANNEL, -1);
        if (!DatabaseUtils.exists(guild, sync, DBEntryKey.AUTOROLE_ID))
            DatabaseUtils.setNumber(guild, sync, DBEntryKey.AUTOROLE_ID, -1);

        reflex.getCustomCommandManager().loadCommands(guild);
        reflex.getLogger().info("Finished setting up guild {} ({})", guild.getName(), guild.getId());
    }

    public void purgeGuild(Guild guild) {
        RedisCommands<String, String> sync = reflex.getDBManager().getSync();
        DatabaseUtils.remove(guild, sync, DBEntryKey.values());
        reflex.getLogger().info("Removed guild {} ({}) from database.", guild.getName(), guild.getId());
    }
}