package me.blayyke.reflex.command.commands.utilities;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandEnvironment;
import net.dv8tion.jda.core.EmbedBuilder;

public class CommandInfo extends AbstractCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.UTILITIES;
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"botinfo", "information", "stats", "statistics"};
    }

    @Override
    public String getDesc() {
        return "Show some information about me";
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