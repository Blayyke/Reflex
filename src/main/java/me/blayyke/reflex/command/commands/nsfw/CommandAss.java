package me.blayyke.reflex.command.commands.nsfw;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandContext;
import me.blayyke.reflex.utils.AbstractCallback;
import net.dv8tion.jda.core.EmbedBuilder;
import okhttp3.Response;
import org.json.JSONArray;

import java.io.IOException;
import java.util.Objects;

public class CommandAss extends AbstractCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.NSFW;
    }

    @Override
    public String getName() {
        return "butts";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"ass", "pussy", "butt"};
    }

    @Override
    public String getDesc() {
        return "Get a random image of butts";
    }

    @Override
    public void execute(CommandContext context) {
        EmbedBuilder embedBuilder = createEmbed();
        String url = "http://api.obutts.ru/noise/1/";
        getReflex().getHttpClient().get(new AbstractCallback() {
            @Override
            public void response(Response response) throws IOException {
                String imageUrl = "http://media.obutts.ru/" + new JSONArray(Objects.requireNonNull(response.body()).string()).getJSONObject(0).getString("preview");

                embedBuilder.setImage(imageUrl);
                embedBuilder.setTitle("Image failed to load? Click here.", imageUrl);
                context.getChannel().sendMessage(embedBuilder.build()).queue();
            }
        }, url);
    }
}
