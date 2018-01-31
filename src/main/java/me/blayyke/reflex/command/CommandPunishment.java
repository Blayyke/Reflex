package me.blayyke.reflex.command;

import me.blayyke.reflex.Colours;
import me.blayyke.reflex.utils.MiscUtils;
import me.blayyke.reflex.utils.ParseUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;

import java.util.List;

public abstract class CommandPunishment extends AbstractCommand {
    @Override
    public void execute(CommandContext context) {
        EmbedBuilder embed = createEmbed(Colours.WARN);

        if (!context.hasArgs()) {
            notEnoughArgs(context);
            return;
        }

        List<Member> members = ParseUtils.findMembers(context.getGuild(), MiscUtils.arrayToString(context.getArgs(), " "));
        if (members.isEmpty()) {
            embed.setTitle("None found");
            embed.setDescription("No members were found! Members can be provided by mention, discord ID or discord tag (Name#1111).");
        } else if (members.size() > 1) {
            embed.setTitle("Multiple found");
            embed.setDescription("Multiple users were found with your search. Please either use a mention, discord ID or discord tag (Name#1111).");
        } else {
            Member member = members.get(0);
            if (!context.getGuild().getSelfMember().canInteract(member)) {
                embed.setTitle("Hierarchy error");
                embed.setDescription("That user cannot be banned their highest role is higher than the bots highest, or they own the server.");
            } else if (!context.getGuild().getSelfMember().hasPermission(getBotRequiredPermissions())) {
                String[] perms = new String[getBotRequiredPermissions().length];
                for (int i = 0; i < getBotRequiredPermissions().length; i++)
                    perms[i] = getBotRequiredPermissions()[i].getName();

                embed.setTitle("Invalid perms");
                embed.setDescription("The bot does not have the required permissions to use this command. Required permission(s): " + MiscUtils.arrayToString(perms, " ") + ". ");
            } else {
                embed.setColor(Colours.INFO);
                embed = applyPunishment(embed, member, context.getMember());
            }
        }

        context.getChannel().sendMessage(embed.build()).queue();
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.MODERATION;
    }

    public abstract Permission[] getBotRequiredPermissions();

    public abstract EmbedBuilder applyPunishment(EmbedBuilder embed, Member member, Member requester);
}