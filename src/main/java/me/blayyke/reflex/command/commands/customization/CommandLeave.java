package me.blayyke.reflex.command.commands.customization;

import me.blayyke.reflex.Colours;
import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandEnvironment;
import me.blayyke.reflex.database.keys.guild.KeyLeaveMessage;
import me.blayyke.reflex.utils.MiscUtils;
import net.dv8tion.jda.core.EmbedBuilder;

public class CommandLeave extends AbstractCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.CUSTOMIZATION;
    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getDesc() {
        return "Change the leave message if enabled.";
    }

    @Override
    public void onCommand(CommandEnvironment env) {
        EmbedBuilder embed = createEmbed(Colours.INFO).setTitle("Leave message");
        String message = getReflex().getDataManager().getGuildStorage(env.getGuild()).getLeaveMessage();

        if (env.hasArgs()) {
            embed.setDescription("The leave message has been updated.");
            embed.addField("Old leave message", message == null ? "None set." : message, true);

            message = MiscUtils.arrayToString(env.getArgs(), " ");
            getReflex().getDBManager().set(new KeyLeaveMessage(env.getGuild()), MiscUtils.equalsAny(message, "none", "off", "disable", "nothing") ? null : message);
            message = getReflex().getDataManager().getGuildStorage(env.getGuild()).getLeaveMessage();
            embed.addField("New leave message", message == null ? "None set." : message, true);

            env.getChannel().sendMessage(embed.build()).queue();
        } else {
            embed.addField("Current leave message", message == null ? "None set." : message, false);
            env.getChannel().sendMessage(embed.build()).queue();
        }
    }
}