package me.blayyke.reflex.command.commands.customization;

import me.blayyke.reflex.Colours;
import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandContext;
import me.blayyke.reflex.database.keys.guild.KeyAnnouncerChannel;
import me.blayyke.reflex.utils.ParseUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;

public class CommandAnnouncer extends AbstractCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.CUSTOMIZATION;
    }

    @Override
    public String getName() {
        return "announcer";
    }

    @Override
    public String getDesc() {
        return "a";
    }

    @Override
    public void onCommand(CommandContext context) {
        EmbedBuilder embed = createEmbed(Colours.INFO).setTitle("Announcer settings");

        if (context.hasArgs()) {
            List<TextChannel> channels = ParseUtils.getTextChannels(context.getGuild(), context.getArgs()[0]);
            if (channels.isEmpty()) {
                embed.setDescription("No channel was found for your input. ");

                context.getChannel().sendMessage(embed.build()).queue();
                return;
            } else if (channels.size() > 1) {
                embed.setColor(Colours.WARN);
                embed.setTitle("Too many found");
                embed.setDescription("More than one channel was found using your input. Please provide a channel id or mention instead");

                context.getChannel().sendMessage(embed.build()).queue();
                return;
            }

            TextChannel oldChannel = getReflex().getDataManager().getGuildStorage(context.getGuild()).getAnnouncerChannel();
            TextChannel newChannel = channels.get(0);
            String oldName = oldChannel == null ? "None set." : oldChannel.getAsMention();
            String newName = newChannel.getAsMention();

            embed.setDescription("The announcer channel has been updated.");
            embed.addField("Old channel", oldName, true);

            getReflex().getDBManager().set(new KeyAnnouncerChannel(context.getGuild()), newChannel.getId());

            embed.addField("New channel", newName, true);

            context.getChannel().sendMessage(embed.build()).queue();
        } else {
            TextChannel channel = getReflex().getDataManager().getGuildStorage(context.getGuild()).getAnnouncerChannel();

            embed.addField("Current channel", channel == null ? "None set." : channel.getAsMention(), true);
            context.getChannel().sendMessage(embed.build()).queue();
        }
    }
}