package me.blayyke.reflex;

import me.blayyke.reflex.command.CommandManager;
import me.blayyke.reflex.command.custom.CustomCommandManager;
import me.blayyke.reflex.database.DBManager;
import me.blayyke.reflex.listeners.*;
import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.User;
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
    private long developerId;
    private HttpClient httpClient;
    private CustomCommandManager customCommandManager;
    private MessageListener messageListener = new MessageListener(this);
    private BotStatsPoster statsPoster;
    private DataManager dataManager;
    private User owner;
    private PointManager pointManager;

    public static void main(String[] args) throws LoginException, IOException {
        new Reflex();
    }

    private Reflex() throws LoginException, IOException {
        dataManager = new DataManager(this);
        dataManager.init();

        httpClient = new HttpClient();

        dbManager = new DBManager(this);
        dbManager.init();

        commandManager = new CommandManager(this);
        commandManager.init();
        customCommandManager = new CustomCommandManager(this);

        statsPoster = new BotStatsPoster(this);

        pointManager = new PointManager(this);

        DefaultShardManagerBuilder builder = new DefaultShardManagerBuilder();
        builder.addEventListeners(new BotListener(this), new JoinLeaveListener(this), messageListener, new ModLogsListener(this), new GuildListener(this));
        builder.setShardsTotal(getDataManager().getSettings().getTotalShardCount());
        builder.setSessionController(new SessionControllerAdapter());
        builder.setToken(getDataManager().getSettings().getToken());
        builder.setGame(Game.playing("Starting up..."));
        builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
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

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public CustomCommandManager getCustomCommandManager() {
        return customCommandManager;
    }

    public MessageListener getMessageListener() {
        return messageListener;
    }

    public BotStatsPoster getStatsPoster() {
        return statsPoster;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getOwner() {
        return owner;
    }

    public PointManager getPointManager() {
        return pointManager;
    }
}