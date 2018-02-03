package me.blayyke.reflex.command.commands.customization;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandContext;
import me.blayyke.reflex.database.DBEntryKey;
import me.blayyke.reflex.utils.DatabaseUtils;
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
    public void execute(CommandContext context) {
        EmbedBuilder embedBuilder = createEmbed();
        embedBuilder.setTitle("Auto-role");

        if (!context.hasArgs()) {
            Role currentAutoRole = getReflex().getShardManager().getRoleById(DatabaseUtils.getNumber(context.getGuild(), getReflex().getDBManager().getSync(), DBEntryKey.AUTOROLE_ID));

            if (currentAutoRole == null) embedBuilder.setDescription("No auto-role currently set.");
            else embedBuilder.setDescription("The current set auto-role is " + currentAutoRole.getName());

            context.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        }

        String input = MiscUtils.arrayToString(context.getArgs(), " ");
        if (MiscUtils.equalsAny(input, "disable", "off", "none", "-1")) {
            DatabaseUtils.setNumber(context.getGuild(), getReflex().getDBManager().getSync(), DBEntryKey.AUTOROLE_ID, -1);
            embedBuilder.setDescription("The auto-role has been turned off.");
            context.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        }

        List<Role> foundRoles = ParseUtils.getRoles(context.getGuild(), input);
        if (foundRoles.isEmpty()) {
            embedBuilder.setDescription("I couldn't find a role with your input. This can be provided by either a role id, mention or name.");
            context.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        } else if (foundRoles.size() > 1) {
            embedBuilder.setDescription("I found more than one role. Please provide a role by either a role id or mention.");
            context.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        }

        Role newRole = foundRoles.get(0);
        if (!context.getGuild().getSelfMember().canInteract(newRole)) {
            embedBuilder.setDescription("I do not have permissions to apply that role. Is it higher than my highest role?");
            context.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        }

        DatabaseUtils.setNumber(context.getGuild(), getReflex().getDBManager().getSync(), DBEntryKey.AUTOROLE_ID, newRole.getIdLong());
        embedBuilder.setDescription("The auto-role has been set to " + newRole.getName() + ".");
        context.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
