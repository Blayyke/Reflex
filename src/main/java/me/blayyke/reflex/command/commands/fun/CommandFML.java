package me.blayyke.reflex.command.commands.fun;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandContext;
import net.dv8tion.jda.core.EmbedBuilder;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;

public class CommandFML extends AbstractCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.FUN;
    }

    @Override
    public String getName() {
        return "fml";
    }

    @Override
    public String getDesc() {
        return "Get a random FML from FMyLife.com";
    }

    @Override
    public void onCommand(CommandContext context) {
        try {
            String address = "http://www.fmylife.com/random";
            Elements element = Jsoup.connect(address).get().select("div.panel-content");
            String text = element.select("p").get(0).text();

            EmbedBuilder embedBuilder = createEmbed();

            embedBuilder.setTitle("FML", "http://www.fmylife.com/random");
            embedBuilder.setDescription(text);
            context.getChannel().sendMessage(embedBuilder.build()).queue();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getCooldown() {
        return 5;
    }
}