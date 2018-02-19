package me.blayyke.reflex.command.commands.customization;

import me.blayyke.reflex.Colours;
import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandContext;
import me.blayyke.reflex.database.keys.guild.KeyWelcomeMessage;
import me.blayyke.reflex.utils.MiscUtils;
import net.dv8tion.jda.core.EmbedBuilder;

public class CommandWelcome extends AbstractCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.CUSTOMIZATION;
    }

    @Override
    public String getName() {
        return "welcome";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"join"};
    }

    @Override
    public String getDesc() {
        return "Change the welcome message if enabled.";
    }

    @Override
    public void execute(CommandContext context) {
        EmbedBuilder embed = createEmbed(Colours.INFO).setTitle("Welcome message");
        String message = getReflex().getDataManager().getGuildStorage(context.getGuild()).getWelcomeMessage();

        if (context.hasArgs()) {
            embed.setDescription("The welcome message has been updated.");
            embed.addField("Old welcome message", message == null ? "None set." : message, true);

            message = MiscUtils.arrayToString(context.getArgs(), " ");
            getReflex().getDBManager().set(new KeyWelcomeMessage(context.getGuild()), MiscUtils.equalsAny(message, "none", "off", "disable", "nothing") ? null : message);
            message = getReflex().getDataManager().getGuildStorage(context.getGuild()).getWelcomeMessage();
            embed.addField("New welcome message", message == null ? "None set." : message, true);

            context.getChannel().sendMessage(embed.build()).queue();
        } else {
            embed.addField("Current welcome message", message == null ? "None set." : message, false);
            context.getChannel().sendMessage(embed.build()).queue();
        }
    }
}