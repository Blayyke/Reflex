package me.blayyke.reflex.command.custom;

import com.lambdaworks.redis.api.sync.RedisCommands;
import me.blayyke.reflex.Colours;
import me.blayyke.reflex.Reflex;
import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandContext;
import me.blayyke.reflex.database.DBEntryKey;
import me.blayyke.reflex.database.DBEntryKeyCCmd;
import me.blayyke.reflex.utils.DatabaseUtils;
import me.blayyke.reflex.utils.MiscUtils;
import me.blayyke.reflex.utils.UserUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class CustomCommandManager {
    private final Reflex reflex;
    private Logger logger = LoggerFactory.getLogger(Reflex.class.getSimpleName() + "-CustomCommandManager");
    private int commandFailures = 0;
    private ScriptEngine executionEngine;
    private int commandsExecuted = 0;

    public CustomCommandManager(Reflex reflex) {
        this.reflex = reflex;
        executionEngine = new ScriptEngineManager().getEngineByName("nashorn");
    }

    public void loadCommands(Guild guild) {
        RedisCommands<String, String> sync = reflex.getDBManager().getSync();
        Set<String> stringSet = DatabaseUtils.getStringSet(guild, sync, DBEntryKey.COMMANDS);

        if (stringSet.isEmpty()) {
            logger.info("Guild {} has no custom commands to load.", guild.getName());
            return;
        }

        for (String name : stringSet) {
            if (name == null) throw new RuntimeException("Command name is null!");

            String desc = DatabaseUtils.getHashString(guild, sync, DBEntryKey.CUSTOM_COMMAND.getRedisKey() + "_" + name, DBEntryKeyCCmd.DESCRIPTION);
            String action = DatabaseUtils.getHashString(guild, sync, DBEntryKey.CUSTOM_COMMAND.getRedisKey() + "_" + name, DBEntryKeyCCmd.ACTION);
            long creatorId = DatabaseUtils.getHashNumber(guild, sync, DBEntryKey.CUSTOM_COMMAND.getRedisKey() + "_" + name, DBEntryKeyCCmd.CREATOR);
            String typeStr = DatabaseUtils.getHashString(guild, sync, DBEntryKey.CUSTOM_COMMAND.getRedisKey() + "_" + name, DBEntryKeyCCmd.TYPE).toUpperCase();
            CustomCommandType type = typeStr.isEmpty() ? null : CustomCommandType.valueOf(typeStr);

            if (action == null)
                action = "channel.sendMessage(\"The action for this command has not been configured. Please contact an administrator.\");";

            CustomCommand command = new CustomCommand(reflex, guild, name);
            command.setReflex(reflex);
            command.setDesc(desc);
            command.setCreatorId(creatorId);
            command.setType(type);
            command.setAction(action);
            loadCommand(command);
        }

        logger.info("Loaded commands for guild {}.", guild.getName());
    }

    public void createCommand(CustomCommand command) {
        if (command == null)
            throw new NullPointerException();
        DatabaseUtils.addToSet(command.getGuild(), reflex.getDBManager().getSync(), DBEntryKey.COMMANDS, command.getName().toLowerCase());
        loadCommand(command);
    }

    private void loadCommand(CustomCommand command) {
        getCommandsForGuild(command.getGuild()).put(command.getName().toLowerCase(), command);
        logger.info("Created/loaded command {} in guild {}.", command.getName(), command.getGuild().getName());
    }

    public CustomCommand getCommand(Guild guild, String commandName) {
        return getCommandsForGuild(guild).get(commandName.toLowerCase());
    }

    public void dispatchCommand(String commandName, String[] args, GuildMessageReceivedEvent event, String prefixUsed) {
        CustomCommand command = getCommand(event.getGuild(), commandName);
        long startTime = System.nanoTime();

        if (command == null) return;
        if (!event.getChannel().canTalk()) return;

        if (!event.getMember().hasPermission(command.getRequiredPermissions())) {
            String[] permArr = new String[command.getRequiredPermissions().length];

            for (int i = 0; i < command.getRequiredPermissions().length; i++)
                permArr[i] = command.getRequiredPermissions()[i].getName();

            event.getChannel().sendMessage(AbstractCommand.createEmbed(Colours.WARN).setTitle("Failure!").setDescription("You do not have the required permissions to execute this command.")
                    .addField("Required permissions", MiscUtils.arrayToString(permArr, ", "), false).build()).queue();
            return;
        }

        executeCommand(startTime, command, new CommandContext(event.getMessage(), event.getMember(), reflex, event.getJDA(), event.getGuild(), commandName, args, prefixUsed));
    }

    private void executeCommand(long startTime, CustomCommand command, CommandContext context) {
        boolean failure = false;
        try {
            executionEngine.put("guild", new BoxedGuild(context.getGuild()));
            executionEngine.put("channel", new BoxedTextChannel(context.getChannel()));
            executionEngine.put("user", new BoxedUser(context.getMember()));
            executionEngine.put("args", context.getArgs());
            executionEngine.put("input", MiscUtils.arrayToString(context.getArgs(), " "));
            command.execute(context);
        } catch (Exception e) {
            commandFailures++;
            logger.warn("[#{}] Custom Command {} failed in guild {} with args {}", commandFailures, command.getName(), context.getGuild().getId(), Arrays.toString(context.getArgs()));
            e.printStackTrace();
            context.getChannel().sendMessage(AbstractCommand.createEmbed(Colours.WARN).setTitle("Error!").setDescription("Something caused this command to error, please notify the developer with the reference number!")
                    .addField("Reference number", "#" + commandFailures, false).build()).queue();
            failure = true;
        }
        commandsExecuted++;
        logger.info("[Execution{}] {} {} {}. Execution took {}ms", failure ? " - FAILURE" : "", UserUtils.formatUser(context.getMessage().getAuthor()), command.getName(), Arrays.toString(context.getArgs()), (Math.round(((double) (System.nanoTime() - startTime)) / 1000) / 100) / 10.0);
    }

    public ScriptEngine getExecutionEngine() {
        return executionEngine;
    }

    private Map<String, CustomCommand> getCommandsForGuild(Guild guild) {
        return reflex.getDataManager().getGuildStorage(guild).getCustomCommandMap();
    }

    public void deleteCommand(CustomCommand c) {
        getCommandsForGuild(c.getGuild()).remove(c.getName().toLowerCase());

        DatabaseUtils.delete(c.getGuild(), reflex.getDBManager().getSync(), DBEntryKey.CUSTOM_COMMAND.getRedisKey() + "_" + c.getName());
        DatabaseUtils.removeFromSet(c.getGuild(), reflex.getDBManager().getSync(), DBEntryKey.COMMANDS, c.getName().toLowerCase());

        logger.info("Deleted command {} in guild {}.", c.getName(), c.getGuild().getName());
    }

    public int getCommandsExecuted() {
        return commandsExecuted;
    }

    public boolean commandExists(Guild guild, String s) {
        return getCommandsForGuild(guild).containsKey(s.toLowerCase());
    }
}