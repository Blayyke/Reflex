package me.blayyke.reflex.command;

import me.blayyke.reflex.Colours;
import me.blayyke.reflex.utils.MiscUtils;
import me.blayyke.reflex.utils.ParseUtils;
import me.blayyke.reflex.utils.TextUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;

import java.util.List;

public abstract class PunishmentCommand extends AbstractCommand {
    public PunishmentCommand(String name, Permission... requiredPermissions) {
        super(CommandCategory.MODERATION, name, TextUtils.uppercaseFirst(name) + " a user", null, requiredPermissions);
    }

    @Override
    public void onCommand(CommandEnvironment env) {
        EmbedBuilder embed = createEmbed(Colours.WARN);

        List<Member> members = ParseUtils.findMembers(env.getGuild(), MiscUtils.arrayToString(env.getArgs(), " "));
        if (!handleMemberList(env, members)) return;
        Member member = members.get(0);
        if (!env.getGuild().getSelfMember().canInteract(member)) {
            embed.setTitle("Hierarchy error");
            embed.setDescription("That user cannot be banned their highest role is higher than the bots highest, or they own the server.");
        } else if (!env.getGuild().getSelfMember().hasPermission(getRequiredPermissions())) {
            String[] perms = new String[getRequiredPermissions().length];
            for (int i = 0; i < getRequiredPermissions().length; i++)
                perms[i] = getRequiredPermissions()[i].getName();

            embed.setTitle("Invalid perms");
            embed.setDescription("The bot does not have the required permissions to use this command. Required permission(s): " + MiscUtils.arrayToString(perms, " ") + ". ");
        } else {
            embed.setColor(Colours.INFO);
            embed = applyPunishment(embed, member, env.getMember());
        }

        env.getChannel().sendMessage(embed.build()).queue();

    }

    public abstract EmbedBuilder applyPunishment(EmbedBuilder embed, Member member, Member requester);

    @Override
    public int getRequiredArgs() {
        return 1;
    }
}