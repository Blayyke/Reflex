package me.blayyke.reflex.command.custom;

import me.blayyke.reflex.Colours;
import me.blayyke.reflex.Reflex;
import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandContext;
import me.blayyke.reflex.utils.MiscUtils;
import me.blayyke.reflex.utils.UserUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CustomCommandManager {
    private final Reflex reflex;
    private Logger logger = LoggerFactory.getLogger(Reflex.class.getSimpleName() + "-CustomCommandManager");
    private HashMap<Long, HashMap<String, CustomCommand>> commandMap = new HashMap<>();
    private int commandFailures = 0;
    private ScriptEngine executionEngine;

    public CustomCommandManager(Reflex reflex) {
        this.reflex = reflex;
        executionEngine = new ScriptEngineManager().getEngineByName("nashorn");
    }

    private void loadCommands() {
        logger.info("Loading commands...");
        logger.info("Commands loaded!");
    }

    private void loadCommand(Guild guild, String name, String desc, String commandCode) {
        CustomCommand command = new CustomCommand(guild, commandCode) {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getDesc() {
                return desc;
            }
        };
        command.setReflex(reflex);

        loadCommand(command);
    }

    private void loadCommand(CustomCommand command) {
        commandMap.get(command.getGuild().getIdLong()).put(command.getName().toLowerCase(), command);
        logger.info("Successfully loaded command {}!", command.getName());
    }

    public HashMap<Long, HashMap<String, CustomCommand>> getCommandMap() {
        return commandMap;
    }

    private CustomCommand getCommand(Guild guild, String commandName) {
        if (!commandMap.containsKey(guild.getIdLong()))
            commandMap.put(guild.getIdLong(), new HashMap<>());

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
            command.execute(context);
        } catch (Exception e) {
            commandFailures++;
            logger.warn("[#{}] Custom Command {} failed in guild {} with args {}", commandFailures, command.getName(), context.getGuild().getId(), Arrays.toString(context.getArgs()));
            e.printStackTrace();
            context.getChannel().sendMessage(AbstractCommand.createEmbed(Colours.WARN).setTitle("Error!").setDescription("Something caused this command to error, please notify the developer with the reference number!")
                    .addField("Reference number", "#" + commandFailures, false).build()).queue();
            failure = true;
        }

        logger.info("[Execution{}] {} {} {}. Execution took {}ms", failure ? " - FAILURE" : "", UserUtils.formatUser(context.getMessage().getAuthor()), command.getName(), Arrays.toString(context.getArgs()), (Math.round(((double) (System.nanoTime() - startTime)) / 1000) / 100) / 10.0);
    }

    public void init() {
//        loadCommands();
    }

    public ScriptEngine getExecutionEngine() {
        return executionEngine;
    }

    public void createCommand(CustomCommand command) {
        if (command == null) {
            System.out.println("Bad command!");
            return;
        }
        command.setReflex(reflex);

        if (!commandMap.containsKey(command.getGuild().getIdLong()))
            commandMap.put(command.getGuild().getIdLong(), new HashMap<>());
        getCommandsForGuild(command.getGuild()).put(command.getName().toLowerCase(), command);
        logger.info("Created command " + command.getName() + " in guild " + command.getGuild().getIdLong());
    }

    private Map<String, CustomCommand> getCommandsForGuild(Guild guild) {
        return commandMap.get(guild.getIdLong());
    }
}