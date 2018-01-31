package me.blayyke.reflex.command.commands.customization;

import me.blayyke.reflex.Colours;
import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandContext;
import me.blayyke.reflex.database.DBEntryKey;
import me.blayyke.reflex.utils.DatabaseUtils;
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
    public void execute(CommandContext context) {
        EmbedBuilder embed = createEmbed(Colours.INFO).setTitle("Leave message");
        String message = getReflex().getLeaveMessage(context.getGuild());

        if (context.hasArgs()) {
            embed.setDescription("The leave message has been updated.");
            embed.addField("Old leave message", message == null ? "None set." : message, true);

            message = MiscUtils.arrayToString(context.getArgs(), " ");

            DatabaseUtils.setString(context.getGuild(), getReflex().getDBManager().getSync(), DBEntryKey.LEAVE_MESSAGE, MiscUtils.equalsAny(message, "none", "off", "disable", "nothing") ? null : message);

            message = getReflex().getLeaveMessage(context.getGuild());
            embed.addField("New leave message", message == null ? "None set." : message, true);

            context.getChannel().sendMessage(embed.build()).queue();
        } else {
            embed.addField("Current leave message", message == null ? "None set." : message, false);
            context.getChannel().sendMessage(embed.build()).queue();
        }
    }
}