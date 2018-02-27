package me.blayyke.reflex.command.commands.utilities;

import me.blayyke.reflex.Colours;
import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandContext;
import net.dv8tion.jda.core.EmbedBuilder;

public class CommandPing extends AbstractCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.UTILITIES;
    }

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getDesc() {
        return "View the bots ping to discord";
    }

    @Override
    public void onCommand(CommandContext context) {
        long startTime = System.currentTimeMillis();
        context.getChannel().sendMessage("Calculating ping...").queue(m -> {
            m.delete().queue();

            EmbedBuilder embed = createEmbed(Colours.INFO);
            embed.setTitle("Ping");
            embed.setDescription("Note: These values represent the _bot's_ ping, not your individual user ping.");
            embed.addField("HTTP Ping", (System.currentTimeMillis() - startTime) + "ms", true);
            embed.addField("WebSocket Ping", context.getJDA().getPing() + "ms", true);

            m.getChannel().sendMessage(embed.build()).queue();
        });
    }
}