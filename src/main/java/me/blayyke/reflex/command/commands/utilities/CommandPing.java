package me.blayyke.reflex.command.commands.utilities;

import me.blayyke.reflex.Colours;
import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandEnvironment;
import net.dv8tion.jda.core.EmbedBuilder;

public class CommandPing extends AbstractCommand {
    public CommandPing() {
        super(CommandCategory.UTILITIES, "ping", "View the bots ping to discord", null);
    }

    @Override
    public void onCommand(CommandEnvironment env) {
        long startTime = System.currentTimeMillis();
        env.getChannel().sendMessage("Calculating ping...").queue(m -> {
            m.delete().queue();

            EmbedBuilder embed = createEmbed(Colours.INFO);
            embed.setTitle("Ping");
            embed.setDescription("Note: These values represent the _bot's_ ping, not your individual user ping.");
            embed.addField("HTTP Ping", (System.currentTimeMillis() - startTime) + "ms", true);
            embed.addField("WebSocket Ping", env.getJDA().getPing() + "ms", true);

            m.getChannel().sendMessage(embed.build()).queue();
        });
    }
}