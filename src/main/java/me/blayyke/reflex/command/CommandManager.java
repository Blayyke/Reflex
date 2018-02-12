package me.blayyke.reflex.command;

import me.blayyke.reflex.Colours;
import me.blayyke.reflex.Reflex;
import me.blayyke.reflex.utils.MiscUtils;
import me.blayyke.reflex.utils.UserUtils;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;

public class CommandManager {
    private final Reflex reflex;
    private Logger logger = LoggerFactory.getLogger(Reflex.class.getSimpleName() + "-CommandManager");
    private HashMap<String, AbstractCommand> commandMap = new HashMap<>();
    private HashMap<String, AbstractCommand> aliasMap = new HashMap<>();
    private int commandFailures = 0;
    private int commandsExecuted = 0;

    public CommandManager(Reflex reflex) {
        this.reflex = reflex;
    }

    private void loadCommands() {
        logger.info("Loading commands...");

        String packageToLookIn = getClass().getPackage().getName() + ".commands";
        Reflections reflections = new Reflections(packageToLookIn);
        reflections.getSubTypesOf(AbstractCommand.class).forEach(c -> {
            if (c.getPackage().getName().startsWith(packageToLookIn))
                loadCommandFromClass(c);
        });

        logger.info("Commands loaded!");
    }

    private void loadCommandFromClass(Class<? extends AbstractCommand> clazz) {
        try {
            AbstractCommand command = clazz.newInstance();
            command.setReflex(reflex);

            loadCommand(command);
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("Failed to load command from class (" + clazz.getName() + ")", e);
        }
    }

    private void loadCommand(AbstractCommand command) {
        if (commandMap.containsKey(command.getName()) || aliasMap.containsKey(command.getName()))
            throw new IllegalStateException("A command is already registered with the name or alias \'" + command.getName() + "\'");

        commandMap.put(command.getName().toLowerCase(), command);

        for (String alias : command.getAliases()) aliasMap.put(alias.toLowerCase(), command);

        logger.info("Successfully loaded command {}!", command.getName());
    }

    public HashMap<String, AbstractCommand> getCommandMap() {
        return commandMap;
    }

    public AbstractCommand getCommand(String commandName) {
        AbstractCommand command = commandMap.get(commandName.toLowerCase());
        if (command != null)
            return command;
        return aliasMap.get(commandName.toLowerCase());
    }

    public void dispatchCommand(String commandName, String[] args, GuildMessageReceivedEvent event, String prefixUsed) {
        AbstractCommand command = getCommand(commandName);
        long startTime = System.nanoTime();
        boolean failure = false;

        if (command == null) {
            reflex.getCustomCommandManager().dispatchCommand(commandName, args, event, prefixUsed);
            return;
        }
        if (!event.getChannel().canTalk()) return;

        if (!event.getMember().hasPermission(command.getRequiredPermissions()) && event.getAuthor().getIdLong() != reflex.getDeveloperId()) {
            String[] permArr = new String[command.getRequiredPermissions().length];

            for (int i = 0; i < command.getRequiredPermissions().length; i++)
                permArr[i] = command.getRequiredPermissions()[i].getName();

            event.getChannel().sendMessage(AbstractCommand.createEmbed(Colours.WARN).setTitle("Failure!").setDescription("You do not have the required permissions to execute this command.")
                    .addField("Required permissions", MiscUtils.arrayToString(permArr, ", "), false).build()).queue();
            return;
        }
        if (!event.getGuild().getSelfMember().hasPermission(command.getBotRequiredPermissions())) {
            String[] permArr = new String[command.getBotRequiredPermissions().length];

            for (int i = 0; i < command.getBotRequiredPermissions().length; i++)
                permArr[i] = command.getBotRequiredPermissions()[i].getName();

            event.getChannel().sendMessage(AbstractCommand.createEmbed(Colours.WARN).setTitle("Failure!").setDescription("I do not have the required permissions to execute this command.")
                    .addField("Required permissions", MiscUtils.arrayToString(permArr, ", "), false).build()).queue();
            return;
        }

        if (command.getCategory() == CommandCategory.DEVELOPER && event.getAuthor().getIdLong() != reflex.getDeveloperId()) {
            logger.info("{} was silently denied access to command {}.", UserUtils.formatUser(event.getAuthor()), command.getName());
            return;
        }
        if (command.getCategory() == CommandCategory.NSFW && !event.getChannel().isNSFW()) {
            event.getChannel().sendMessage(AbstractCommand.createEmbed(Colours.WARN).setTitle("Failure!").setDescription("NSFW commands may not be used outside of NSFW-enabled channels.").build()).queue();
            return;
        }

        try {
            command.execute(new CommandContext(event.getMessage(), event.getMember(), reflex, event.getJDA(), event.getGuild(), commandName, args, prefixUsed));
        } catch (Exception e) {
            commandFailures++;
            logger.warn("[#{}] Command {} failed in guild {} with args {}", commandFailures, command.getName(), event.getGuild().getId(), Arrays.toString(args));
            e.printStackTrace();
            event.getChannel().sendMessage(AbstractCommand.createEmbed(Colours.WARN).setTitle("Error!").setDescription("Something caused this command to error, please notify the developer with the reference number!")
                    .addField("Reference number", "#" + commandFailures, false).build()).queue();
            failure = true;
        }

        commandsExecuted++;
        logger.info("[Execution{}] {} {} {}. Execution took {}ms", failure ? " - FAILURE" : "", UserUtils.formatUser(event.getAuthor()), command.getName(), Arrays.toString(args), (Math.round(((double) (System.nanoTime() - startTime)) / 1000) / 100) / 10.0);
    }

    public void init() {
        loadCommands();
    }

    public int getCommandsExecuted() {
        return commandsExecuted;
    }
}