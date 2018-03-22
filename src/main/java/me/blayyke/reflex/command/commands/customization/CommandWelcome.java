package me.blayyke.reflex.command.commands.customization;

import me.blayyke.reflex.Colours;
import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandEnvironment;
import me.blayyke.reflex.database.keys.guild.KeyWelcomeMessage;
import me.blayyke.reflex.utils.MiscUtils;
import net.dv8tion.jda.core.EmbedBuilder;

public class CommandWelcome extends AbstractCommand {
    public CommandWelcome() {
        super(CommandCategory.CUSTOMIZATION, "welcome", "Change the welcome message if enabled", new String[]{"join"});
    }

    @Override
    public void onCommand(CommandEnvironment env) {
        EmbedBuilder embed = createEmbed(Colours.INFO).setTitle("Welcome message");
        String message = getReflex().getDataManager().getGuildStorage(env.getGuild()).getWelcomeMessage();

        if (env.hasArgs()) {
            embed.setDescription("The welcome message has been updated.");
            embed.addField("Old welcome message", message == null ? "None set." : message, true);

            message = MiscUtils.arrayToString(env.getArgs(), " ");
            getReflex().getDBManager().set(new KeyWelcomeMessage(env.getGuild()), MiscUtils.equalsAny(message, "none", "off", "disable", "nothing") ? null : message);
            message = getReflex().getDataManager().getGuildStorage(env.getGuild()).getWelcomeMessage();
            embed.addField("New welcome message", message == null ? "None set." : message, true);

            env.getChannel().sendMessage(embed.build()).queue();
        } else {
            embed.addField("Current welcome message", message == null ? "None set." : message, false);
            env.getChannel().sendMessage(embed.build()).queue();
        }
    }
}