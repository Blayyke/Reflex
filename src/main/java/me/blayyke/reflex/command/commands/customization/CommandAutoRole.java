package me.blayyke.reflex.command.commands.customization;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandContext;
import me.blayyke.reflex.database.keys.guild.KeyAutoRole;
import me.blayyke.reflex.utils.MiscUtils;
import me.blayyke.reflex.utils.ParseUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;

import java.util.List;

public class CommandAutoRole extends AbstractCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.CUSTOMIZATION;
    }

    @Override
    public Permission[] getBotRequiredPermissions() {
        return new Permission[]{Permission.MANAGE_ROLES};
    }

    @Override
    public String getName() {
        return "autorole";
    }

    @Override
    public String getDesc() {
        return "Set or disable the role given to new members";
    }

    @Override
    public void onCommand(CommandContext context) {
        EmbedBuilder embedBuilder = createEmbed();
        embedBuilder.setTitle("Auto-role");

        if (!context.hasArgs()) {
            Role autoRole = getReflex().getDataManager().getGuildStorage(context.getGuild()).getAutoRole();

            if (autoRole == null) embedBuilder.setDescription("No auto-role currently set.");
            else embedBuilder.setDescription("The current set auto-role is " + autoRole.getName());

            context.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        }

        String input = MiscUtils.arrayToString(context.getArgs(), " ");
        if (MiscUtils.equalsAny(input, "disable", "off", "none", "-1")) {
            getReflex().getDBManager().set(new KeyAutoRole(context.getGuild()), String.valueOf(-1));
            embedBuilder.setDescription("The auto-role has been turned off.");
            context.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        }

        List<Role> foundRoles = ParseUtils.getRoles(context.getGuild(), input);
        if (foundRoles.isEmpty()) {
            replyError(context, "No role found with your input.");
            return;
        } else if (foundRoles.size() > 1) {
            replyError(context, "More than one role was found with the provided input. Please be more specific (ID or mention).");
            return;
        }

        Role newRole = foundRoles.get(0);
        if (!context.getGuild().getSelfMember().canInteract(newRole)) {
            replyError(context, "I do not have permissions to apply that role. Is it higher than my highest role?");
            return;
        }

        getReflex().getDBManager().set(new KeyAutoRole(context.getGuild()), newRole.getId());
        embedBuilder.setDescription("The auto-role has been set to " + newRole.getName() + ".");
        context.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
