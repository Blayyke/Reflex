package me.blayyke.reflex.settings;

import me.blayyke.reflex.Reflex;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class BotSettings {
    private final Reflex bot;
    private final File file;

    private String token;
    private String defaultPrefix;
    private String gameName;
    private int totalShardCount;
    private DBSettings databaseSettings;

    public BotSettings(Reflex bot) {
        this.bot = bot;

        file = new File("settings.json");
        try {
            if (file.createNewFile()) Files.write(file.toPath(), "{}".getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Reflex getBot() {
        return bot;
    }

    public String getDefaultPrefix() {
        return defaultPrefix;
    }

    public String getToken() {
        return token;
    }

    public void load() throws IOException {
        JSONObject data = new JSONObject(new String(Files.readAllBytes(file.toPath())));

        this.defaultPrefix = readFromJson(data, "default_prefix", "r!");
        this.token = readFromJson(data, "token", "");
        this.gameName = readFromJson(data, "game", "Hello world!");
        this.totalShardCount = readFromJson(data, "total_shards", 1);

        JSONObject dbSettings = readFromJson(data, "db_settings", new JSONObject()
                .put("port", 6379)
                .put("host", "localhost")
                .put("db_number", 0)
                .put("password", JSONObject.NULL));

        databaseSettings = new DBSettings();
        databaseSettings.setDatabasePort(dbSettings.getInt("port"));
        databaseSettings.setDatabaseHost(dbSettings.getString("host"));
        databaseSettings.setDatabaseNumber(dbSettings.getInt("db_number"));
        databaseSettings.setDatabasePassword(dbSettings.isNull("password") ? null : dbSettings.getString("password"));
    }

    @SuppressWarnings("unchecked")
    private <A> A readFromJson(JSONObject object, String key, A defaultValue) throws IOException {
        if (!object.has(key) || object.isNull(key)) {
            object.put(key, defaultValue);
            Files.write(file.toPath(), object.toString(2).getBytes());
        }

        return (A) object.get(key);
    }

    public int getTotalShardCount() {
        return totalShardCount;
    }

    public String getGameName() {
        return gameName;
    }

    public DBSettings getDatabaseSettings() {
        return databaseSettings;
    }
}