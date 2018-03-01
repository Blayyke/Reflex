package me.blayyke.reflex.command.commands.utilities;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandEnvironment;
import me.blayyke.reflex.utils.MiscUtils;
import me.blayyke.reflex.utils.ParseUtils;
import me.blayyke.reflex.utils.TextUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Role;

import java.util.List;

public class CommandRoleInfo extends AbstractCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.UTILITIES;
    }

    @Override
    public String getName() {
        return "roleinfo";
    }

    @Override
    public int getRequiredArgs() {
        return 1;
    }

    @Override
    public String[] getAliases() {
        return new String[]{"role", "ri"};
    }

    @Override
    public String getDesc() {
        return "View information about the provided role";
    }


    @Override
    protected void onCommand(CommandEnvironment env) {
        List<Role> roles = ParseUtils.getRoles(env.getGuild(), MiscUtils.arrayToString(env.getArgs(), " "));
        if (!handleRoleList(env, roles)) return;

        Role role = roles.get(0);
        EmbedBuilder embed = createEmbed();

        String createDate = TextUtils.formatDatePretty(role.getCreationTime());

        embed.setTitle("Role info for " + role.getName());
        embed.setColor(role.getColor());

        embed.addField("ID", role.getId(), true);
        embed.addField("Creation Date", createDate, true);

        embed.addField("Position", String.valueOf(role.getPosition()), true);
        embed.addField("Hoisted", role.isHoisted() ? "Yes" : "No", true);

        embed.addField("Managed", role.isManaged() ? "Yes" : "No", true);
        embed.addField("Mentionable", role.isMentionable() ? "Yes" : "No", true);

        env.getChannel().sendMessage(embed.build()).queue();
    }
}