package me.blayyke.reflex;

import me.blayyke.reflex.command.CommandManager;
import me.blayyke.reflex.command.custom.CustomCommandManager;
import me.blayyke.reflex.database.DBEntryKey;
import me.blayyke.reflex.database.DBManager;
import me.blayyke.reflex.listeners.BotListener;
import me.blayyke.reflex.listeners.GuildListener;
import me.blayyke.reflex.listeners.JoinLeaveListener;
import me.blayyke.reflex.listeners.MessageListener;
import me.blayyke.reflex.settings.BotSettings;
import me.blayyke.reflex.utils.DatabaseUtils;
import me.blayyke.reflex.utils.StackTraceHelper;
import me.blayyke.reflex.utils.Version;
import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.utils.SessionControllerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class Reflex {
    private final ShardManager shardManager;
    private final CommandManager commandManager;
    private final DBManager dbManager;
    private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    private BotSettings settings;
    private long developerId;
    private StackTraceHelper stackTraceHelper = new StackTraceHelper(getClass().getPackage().getName());
    private HttpClient httpClient;
    private CustomCommandManager customCommandManager;
    private MessageListener messageListener = new MessageListener(this);

    public static void main(String[] args) throws LoginException, IOException {
        new Reflex();
    }

    private Reflex() throws LoginException, IOException {
        settings = new BotSettings(this);
        settings.load();

        Version.loadVersion();

        httpClient = new HttpClient();

        dbManager = new DBManager(this);
        dbManager.init();

        commandManager = new CommandManager(this);
        commandManager.init();
        customCommandManager = new CustomCommandManager(this);

        DefaultShardManagerBuilder builder = new DefaultShardManagerBuilder();
        builder.setShardsTotal(settings.getTotalShardCount());
        builder.addEventListeners(new BotListener(this), new JoinLeaveListener(this), messageListener, new GuildListener(this));
        builder.setGame(Game.playing("Starting up..."));
        builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
        builder.setSessionController(new SessionControllerAdapter());
        builder.setToken(settings.getToken());
        this.shardManager = builder.build();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            shardManager.shutdown();
            dbManager.shutdown();
        }));
    }

    public Logger getLogger() {
        return logger;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public BotSettings getSettings() {
        return settings;
    }

    public DBManager getDBManager() {
        return dbManager;
    }

    public ShardManager getShardManager() {
        return shardManager;
    }

    public long getDeveloperId() {
        return developerId;
    }

    public void setDeveloperId(long developerId) {
        this.developerId = developerId;
    }

    public StackTraceHelper getStackTraceHelper() {
        return stackTraceHelper;
    }

    public String getPrefix(Guild guild) {
        return DatabaseUtils.getString(guild, dbManager.getSync(), DBEntryKey.GUILD_PREFIX);
    }

    public String getWelcomeMessage(Guild guild) {
        return DatabaseUtils.getString(guild, dbManager.getSync(), DBEntryKey.JOIN_MESSAGE);
    }

    public String getLeaveMessage(Guild guild) {
        return DatabaseUtils.getString(guild, dbManager.getSync(), DBEntryKey.LEAVE_MESSAGE);
    }

    public long getAnnouncerChannelId(Guild guild) {
        return DatabaseUtils.getNumber(guild, dbManager.getSync(), DBEntryKey.ANNOUNCEMENT_CHANNEL);
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public CustomCommandManager getCustomCommandManager() {
        return customCommandManager;
    }

    public MessageListener getMessageListener() {
        return messageListener;
    }
}