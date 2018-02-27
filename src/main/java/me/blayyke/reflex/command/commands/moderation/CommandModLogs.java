package me.blayyke.reflex.command.commands.moderation;

import me.blayyke.reflex.Colours;
import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandContext;
import me.blayyke.reflex.utils.MiscUtils;
import me.blayyke.reflex.utils.ParseUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;

public class CommandModLogs extends AbstractCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.MODERATION;
    }

    @Override
    public String getName() {
        return "modlogs";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"modlog", "ml"};
    }

    @Override
    public String getDesc() {
        return "Log to a mod-logs channel whenever a moderator action is issued.";
    }

    @Override
    public void onCommand(CommandContext context) {
        if (!context.hasArgs()) {
            notEnoughArgs(context);
            return;
        }

        List<TextChannel> channels = ParseUtils.getTextChannels(context.getGuild(), MiscUtils.arrayToString(context.getArgs(), "-"));
        EmbedBuilder embed = createEmbed();
        embed.setTitle("Mod-Logs");

        if (channels.isEmpty()) {
            embed.setColor(Colours.WARN);
            embed.setDescription("No channels were found! Channels can be provided by mention, ID or name.");
            return;
        }
        if (channels.size() > 1) {
            embed.setColor(Colours.WARN);
            embed.setDescription("Multiple channels were found with your search. Please either use a mention or ID.");
            return;
        }

        TextChannel channel = channels.get(0);
        getReflex().getDataManager().getGuildStorage(context.getGuild()).setModLogChannel(channel);

        embed.setDescription("The mod-logs channel has been set to " + channel.getAsMention());

        context.getChannel().sendMessage(embed.build()).queue();
    }
}