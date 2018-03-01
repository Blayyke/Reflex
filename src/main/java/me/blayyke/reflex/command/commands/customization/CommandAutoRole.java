package me.blayyke.reflex.command.commands.customization;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandEnvironment;
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
    public void onCommand(CommandEnvironment env) {
        EmbedBuilder embedBuilder = createEmbed();
        embedBuilder.setTitle("Auto-role");

        if (!env.hasArgs()) {
            Role autoRole = getReflex().getDataManager().getGuildStorage(env.getGuild()).getAutoRole();

            if (autoRole == null) embedBuilder.setDescription("No auto-role currently set.");
            else embedBuilder.setDescription("The current set auto-role is " + autoRole.getName());

            env.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        }

        String input = MiscUtils.arrayToString(env.getArgs(), " ");
        if (MiscUtils.equalsAny(input, "disable", "off", "none", "-1")) {
            getReflex().getDBManager().set(new KeyAutoRole(env.getGuild()), String.valueOf(-1));
            embedBuilder.setDescription("The auto-role has been turned off.");
            env.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        }

        List<Role> foundRoles = ParseUtils.getRoles(env.getGuild(), input);
        if (foundRoles.isEmpty()) {
            replyError(env, "No role found with your input.");
            return;
        } else if (foundRoles.size() > 1) {
            replyError(env, "More than one role was found with the provided input. Please be more specific (ID or mention).");
            return;
        }

        Role newRole = foundRoles.get(0);
        if (!env.getGuild().getSelfMember().canInteract(newRole)) {
            replyError(env, "I do not have permissions to apply that role. Is it higher than my highest role?");
            return;
        }

        getReflex().getDBManager().set(new KeyAutoRole(env.getGuild()), newRole.getId());
        embedBuilder.setDescription("The auto-role has been set to " + newRole.getName() + ".");
        env.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
