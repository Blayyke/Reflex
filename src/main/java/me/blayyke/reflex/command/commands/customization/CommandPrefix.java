package me.blayyke.reflex.command.commands.customization;

import me.blayyke.reflex.Colours;
import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandContext;
import me.blayyke.reflex.database.DBEntryKey;
import me.blayyke.reflex.utils.DatabaseUtils;
import me.blayyke.reflex.utils.MiscUtils;
import net.dv8tion.jda.core.EmbedBuilder;

public class CommandPrefix extends AbstractCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.CUSTOMIZATION;
    }

    @Override
    public String getName() {
        return "prefix";
    }

    @Override
    public String getDesc() {
        return "View or change the bots command prefix";
    }

    @Override
    public void execute(CommandContext context) {
        EmbedBuilder embed = createEmbed(Colours.INFO).setTitle("Prefix");

        if (context.hasArgs()) {
            embed.setDescription("The prefix has been updated.");
            embed.addField("Old prefix", MiscUtils.escapeFormatting(getReflex().getPrefix(context.getGuild())), true);
            DatabaseUtils.setString(context.getGuild(), getReflex().getDBManager().getSync(), DBEntryKey.GUILD_PREFIX, MiscUtils.arrayToString(context.getArgs(), " ").toLowerCase());
            embed.addField("New prefix", MiscUtils.escapeFormatting(getReflex().getPrefix(context.getGuild())), true);

            context.getChannel().sendMessage(embed.build()).queue();
        } else {
            embed.addField("Current prefix", getReflex().getPrefix(context.getGuild()), true);
            embed.setDescription("You can also mention the bot to use as a prefix.");
            context.getChannel().sendMessage(embed.build()).queue();
        }
    }
}