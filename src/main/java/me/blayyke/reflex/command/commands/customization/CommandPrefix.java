package me.blayyke.reflex.command.commands.customization;

import me.blayyke.reflex.Colours;
import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandContext;
import me.blayyke.reflex.database.keys.guild.KeyPrefix;
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
    public void onCommand(CommandContext context) {
        EmbedBuilder embed = createEmbed(Colours.INFO).setTitle("Prefix");

        if (context.hasArgs()) {
            embed.setDescription("The prefix has been updated.");
            embed.addField("Old prefix", MiscUtils.escapeFormatting(getReflex().getDataManager().getGuildStorage(context.getGuild()).getPrefix()), true);

            getReflex().getDBManager().set(new KeyPrefix(context.getGuild()), MiscUtils.arrayToString(context.getArgs(), " ").replace("_", " ").toLowerCase());
            embed.addField("New prefix", "`" + MiscUtils.escapeFormatting(getReflex().getDataManager().getGuildStorage(context.getGuild()).getPrefix()) + "`", true);

            context.getChannel().sendMessage(embed.build()).queue();
        } else {
            embed.addField("Current prefix", "`" + MiscUtils.escapeFormatting(getReflex().getDataManager().getGuildStorage(context.getGuild()).getPrefix()) + "`", true);
            embed.setDescription("You can also mention the bot to use as a prefix.");
            context.getChannel().sendMessage(embed.build()).queue();
        }
    }
}