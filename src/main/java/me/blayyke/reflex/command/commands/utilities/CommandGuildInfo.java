package me.blayyke.reflex.command.commands.utilities;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandEnvironment;
import me.blayyke.reflex.utils.UserUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;

public class CommandGuildInfo extends AbstractCommand {
    public CommandGuildInfo() {
        super(CommandCategory.UTILITIES, "guildinfo", "View information about a role", new String[]{"guild", "gi", "server", "si", "serverinfo"});
    }

    @Override
    public void onCommand(CommandEnvironment env) {
        EmbedBuilder embedBuilder = createEmbed();
        Guild guild = env.getGuild();

        embedBuilder.setTitle("Guild Information");
        embedBuilder.setThumbnail(guild.getIconUrl());
        embedBuilder.setDescription("Here is some information about this guild.");

        embedBuilder.addField("Owner", UserUtils.formatUser(guild.getOwner().getUser()), true);
        embedBuilder.addField("ID", guild.getId(), true);

        embedBuilder.addField("Region", guild.getRegion().getName(), true);
        embedBuilder.addField("Members", String.valueOf(guild.getMembers().size()), true);

        embedBuilder.addField("2FA Enabled", (guild.getRequiredMFALevel() == Guild.MFALevel.TWO_FACTOR_AUTH ? "Yes" : "No"), true);
        embedBuilder.addField("Voice AFK Timeout", guild.getAfkTimeout().getSeconds() / 60 + " minutes", true);

        env.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}