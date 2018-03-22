package me.blayyke.reflex.command.commands.customization;

import me.blayyke.reflex.Colours;
import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandEnvironment;
import me.blayyke.reflex.database.keys.guild.KeyAnnouncerChannel;
import me.blayyke.reflex.utils.ParseUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;

public class CommandAnnouncer extends AbstractCommand {
    public CommandAnnouncer() {
        super(CommandCategory.CUSTOMIZATION, "announcer", "Set the channel where user join/leave announcements are sent", null);
    }

    @Override
    public void onCommand(CommandEnvironment env) {
        EmbedBuilder embed = createEmbed(Colours.INFO).setTitle("Announcer settings");

        if (env.hasArgs()) {
            List<TextChannel> channels = ParseUtils.getTextChannels(env.getGuild(), env.getArgs()[0]);
            if (channels.isEmpty()) {
                replyError(env, "No channel found with your input.");
                return;
            } else if (channels.size() > 1) {
                replyError(env, "More than one channel was found with the provided input. Please be more specific (ID or mention).");
                return;
            }

            TextChannel oldChannel = getReflex().getDataManager().getGuildStorage(env.getGuild()).getAnnouncerChannel();
            TextChannel newChannel = channels.get(0);
            String oldName = oldChannel == null ? "None set." : oldChannel.getAsMention();
            String newName = newChannel.getAsMention();

            embed.setDescription("The announcer channel has been updated.");
            embed.addField("Old channel", oldName, true);

            getReflex().getDBManager().set(new KeyAnnouncerChannel(env.getGuild()), newChannel.getId());

            embed.addField("New channel", newName, true);

            env.getChannel().sendMessage(embed.build()).queue();
        } else {
            TextChannel channel = getReflex().getDataManager().getGuildStorage(env.getGuild()).getAnnouncerChannel();

            embed.addField("Current channel", channel == null ? "None set." : channel.getAsMention(), true);
            env.getChannel().sendMessage(embed.build()).queue();
        }
    }
}