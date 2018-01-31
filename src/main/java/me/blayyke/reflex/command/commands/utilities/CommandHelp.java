package me.blayyke.reflex.command.commands.utilities;

import me.blayyke.reflex.Colours;
import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandContext;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;

import java.util.Arrays;

public class CommandHelp extends AbstractCommand {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDesc() {
        return "View help for commands and/or categories in this bot";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.UTILITIES;
    }

    @Override
    public void execute(CommandContext context) {
        if (!context.hasArgs()) {
            EmbedBuilder embed = createEmbed(Colours.INFO);
            embed.setTitle("Bot help");
            embed.setDescription(context.getJDA().getSelfUser().getName() + " is a bot designed to be useful for a range of purposes, with a suite of commands ranging from moderation commands (kick, ban, etc.) commands to random image commands (dogs, cats, ect).");

            StringBuilder builder = new StringBuilder();
            for (CommandCategory commandCategory : CommandCategory.values())
                builder.append("- ").append(commandCategory.getName()).append("\n");
            String categoryStr = builder.toString();


            embed.addField("Categories", categoryStr.substring(0, categoryStr.length() - 1), false);

            context.getChannel().sendMessage(embed.build()).queue();
            return;
        }

        // Has args
        CommandCategory category = Arrays.stream(CommandCategory.values()).filter(c -> c.getName().equalsIgnoreCase(context.getArgs()[0])).findFirst().orElse(null);
        if (category != null) {
            EmbedBuilder embed = createEmbed(Colours.INFO);
            StringBuilder builder = new StringBuilder();

            embed.setTitle("Bot help");
            embed.setDescription("**" + category.getName() + "** - " + category.getDescription());

            getReflex().getCommandManager().getCommandMap().values().stream().filter(cmd -> cmd.getCategory() == category).forEach(cmd -> builder.append(cmd.getName().toLowerCase()).append(", "));
            String cmdStr = builder.toString();

            embed.addField("Commands", cmdStr.substring(0, cmdStr.length() - 2), false);

            context.getChannel().sendMessage(embed.build()).queue();
            return;
        }

        AbstractCommand cmd = getReflex().getCommandManager().getCommand(context.getArgs()[0]);
        if (cmd != null) {
            EmbedBuilder embed = createEmbed(Colours.INFO);
            embed.setTitle("Bot help");
            embed.setDescription(getReflex().getPrefix(context.getGuild()).toLowerCase() + "**" + cmd.getName() + "** - " + cmd.getDesc());

            if (cmd.getRequiredPermissions().length > 0) {
                StringBuilder builder = new StringBuilder();
                for (Permission c : cmd.getRequiredPermissions())
                    builder.append(c.getName().toLowerCase()).append(", ");
                String perms = builder.toString();

                embed.addField("Required permissions", perms.substring(0, perms.length() - 2), false);
            } else
                embed.addField("Required permissions", "No permissions required.", false);


            embed.addField("Category", cmd.getCategory().getName(), true);

            if (cmd.getAliases().length > 0) {
                String aliases;
                StringBuilder b = new StringBuilder();
                for (int i = 0; ; i++) {
                    b.append(String.valueOf(cmd.getAliases()[i]));
                    if (i == cmd.getAliases().length - 1) {
                        aliases = b.toString();
                        break;
                    }
                    b.append(", ");
                }
                embed.addField("Aliases", aliases.substring(1, aliases.length() - 1), true);
            } else
                embed.addField("Aliases", "No aliases.", true);

            context.getChannel().sendMessage(embed.build()).queue();
            return;
        }

        // No category or cmd found
        EmbedBuilder embed = createEmbed(Colours.WARN);
        embed.setTitle("Bot help");
        embed.setDescription("`" + context.getArgs()[0] + "` is not a valid command name or command category.");
        context.getChannel().sendMessage(embed.build()).queue();
    }
}