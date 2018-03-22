package me.blayyke.reflex.command.commands.utilities;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandEnvironment;
import me.blayyke.reflex.utils.MiscUtils;
import me.blayyke.reflex.utils.ParseUtils;
import me.blayyke.reflex.utils.TextUtils;
import me.blayyke.reflex.utils.UserUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.util.List;

public class CommandUserInfo extends AbstractCommand {
    public CommandUserInfo() {
        super(CommandCategory.UTILITIES, "userinfo", "View information about a user", new String[]{"user", "member", "ui"});
    }

    @Override
    public int getRequiredArgs() {
        return 1;
    }

    @Override
    protected void onCommand(CommandEnvironment env) {
        List<Member> members = ParseUtils.findMembers(env.getGuild(), MiscUtils.arrayToString(env.getArgs(), " "));
        Member member = members.get(0);
        EmbedBuilder embed = createEmbed();

        String roleStr = roleStr(member.getRoles());
        String joinDate = TextUtils.formatDatePretty(member.getJoinDate());
        String createDate = TextUtils.formatDatePretty((member.getUser().getCreationTime()));

        embed.setTitle("User info for " + UserUtils.formatUser(member.getUser()));
        embed.setColor(member.getColor());
        embed.setThumbnail(member.getUser().getAvatarUrl());

        embed.addField("Nickname", member.getEffectiveName(), true);
        embed.addField("ID", member.getUser().getId(), true);

        embed.addField("Status", TextUtils.uppercaseFirst(member.getOnlineStatus().getKey()), true);
        embed.addField("Join Date", joinDate, true);

        if (member.getGame() != null)
            embed.addField("Game", member.getGame().getName(), true);
        embed.addField("Creation Date", createDate, true);

        embed.addField("Roles", roleStr, false);

        env.getChannel().sendMessage(embed.build()).queue();
    }

    private String roleStr(List<Role> roles) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; ; i++) {
            builder.append(roles.get(i).getName());
            if (i == roles.size() - 1) break;
            builder.append(", ");
        }

        return builder.toString();
    }
}