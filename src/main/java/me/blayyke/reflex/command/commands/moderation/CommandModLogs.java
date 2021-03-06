package me.blayyke.reflex.command.commands.moderation;

import me.blayyke.reflex.Colours;
import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandEnvironment;
import me.blayyke.reflex.utils.MiscUtils;
import me.blayyke.reflex.utils.ParseUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;

public class CommandModLogs extends AbstractCommand {
    public CommandModLogs() {
        super(CommandCategory.MODERATION, "modlogs", "Set the channel to notify in whenever a moderator action is issued", new String[]{"ml"});
    }

    @Override
    public void onCommand(CommandEnvironment env) {
        List<TextChannel> channels = ParseUtils.getTextChannels(env.getGuild(), MiscUtils.arrayToString(env.getArgs(), "-"));
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
        getReflex().getDataManager().getGuildStorage(env.getGuild()).setModLogChannel(channel);

        embed.setDescription("The mod-logs channel has been set to " + channel.getAsMention());

        env.getChannel().sendMessage(embed.build()).queue();
    }

    @Override
    public int getRequiredArgs() {
        return 1;
    }
}