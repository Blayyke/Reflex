package me.blayyke.reflex.command.commands.customization;

import me.blayyke.reflex.Colours;
import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandEnvironment;
import me.blayyke.reflex.database.keys.guild.KeyPrefix;
import me.blayyke.reflex.utils.MiscUtils;
import me.blayyke.reflex.utils.TextUtils;
import net.dv8tion.jda.core.EmbedBuilder;

public class CommandPrefix extends AbstractCommand {
    public CommandPrefix() {
        super(CommandCategory.CUSTOMIZATION, "prefix", "View or change the bots command prefix", null);
    }

    @Override
    public void onCommand(CommandEnvironment env) {
        EmbedBuilder embed = createEmbed(Colours.INFO).setTitle("Prefix");

        if (env.hasArgs()) {
            embed.setDescription("The prefix has been updated.");
            embed.addField("Old prefix", TextUtils.escapeFormatting(getReflex().getDataManager().getGuildStorage(env.getGuild()).getPrefix()), true);

            getReflex().getDBManager().set(new KeyPrefix(env.getGuild()), MiscUtils.arrayToString(env.getArgs(), " ").replace("_", " ").toLowerCase());
            embed.addField("New prefix", "`" + TextUtils.escapeFormatting(getReflex().getDataManager().getGuildStorage(env.getGuild()).getPrefix()) + "`", true);

            env.getChannel().sendMessage(embed.build()).queue();
        } else {
            embed.addField("Current prefix", "`" + TextUtils.escapeFormatting(getReflex().getDataManager().getGuildStorage(env.getGuild()).getPrefix()) + "`", true);
            embed.setDescription("You can also mention the bot to use as a prefix.");
            env.getChannel().sendMessage(embed.build()).queue();
        }
    }
}