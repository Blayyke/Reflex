package me.blayyke.reflex.command.commands.utilities;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandEnvironment;
import net.dv8tion.jda.core.EmbedBuilder;

public class CommandInfo extends AbstractCommand {
    public CommandInfo() {
        super(CommandCategory.UTILITIES, "stats", "Show some bot statistics/information", new String[]{"statistics", "botinfo"});
    }

    @Override
    public void onCommand(CommandEnvironment env) {
        EmbedBuilder embedBuilder = createEmbed();

        embedBuilder.setTitle("Bot information / statistics");

        embedBuilder.addField("Commands executed", String.valueOf(getReflex().getCommandManager().getCommandsExecuted()), true);
        embedBuilder.addField("Custom Commands ran", String.valueOf(getReflex().getCommandManager().getCommandsExecuted()), true);
        embedBuilder.addField("Messages received", String.valueOf(getReflex().getMessageListener().getMessagesReceived()), true);

        embedBuilder.addField("Guilds", String.valueOf(getReflex().getShardManager().getGuilds().size()), true);
        embedBuilder.addField("Text Channels", String.valueOf(getReflex().getShardManager().getTextChannels().size()), true);
        embedBuilder.addField("Voice Channels", String.valueOf(getReflex().getShardManager().getVoiceChannels().size()), true);

        embedBuilder.addBlankField(true);
        embedBuilder.addField("Channel Categories", String.valueOf(getReflex().getShardManager().getCategories().size()), true);
        embedBuilder.addBlankField(true);

        env.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}