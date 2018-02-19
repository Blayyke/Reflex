package me.blayyke.reflex.command.commands.utilities;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandContext;
import me.blayyke.reflex.utils.UserUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;

public class CommandGuildInfo extends AbstractCommand {
    @Override
    public String getName() {
        return "guildinfo";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.UTILITIES;
    }

    @Override
    public String getDesc() {
        return "View information about this guild";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"guild", "gi", "ginfo"};
    }

    @Override
    public void execute(CommandContext context) {
        EmbedBuilder embedBuilder = createEmbed();
        Guild guild = context.getGuild();

        embedBuilder.setTitle("Guild Information");
        embedBuilder.setThumbnail(guild.getIconUrl());
        embedBuilder.setDescription("Here is some information about this guild.");

        embedBuilder.addField("Owner", UserUtils.formatUser(guild.getOwner().getUser()), true);
        embedBuilder.addField("ID", guild.getId(), true);

        embedBuilder.addField("Region", guild.getRegion().getName(), true);
        embedBuilder.addField("Members", String.valueOf(guild.getMembers().size()), true);

        embedBuilder.addField("2FA Enabled", (guild.getRequiredMFALevel() == Guild.MFALevel.TWO_FACTOR_AUTH ? "Yes" : "No"), true);
        embedBuilder.addField("Voice AFK Timeout", guild.getAfkTimeout().getSeconds() + " seconds", true);

        context.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}