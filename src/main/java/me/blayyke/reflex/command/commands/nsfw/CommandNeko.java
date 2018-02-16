package me.blayyke.reflex.command.commands.nsfw;

import me.blayyke.reflex.Colours;
import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandContext;
import me.blayyke.reflex.utils.AbstractCallback;
import me.blayyke.reflex.utils.MiscUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

public class CommandNeko extends AbstractCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.NSFW;
    }

    @Override
    public String getName() {
        return "neko";
    }

    @Override
    public String getDesc() {
        return "Send an image from nekos.brussell.me with your specified tags";
    }

    @Override
    public void execute(CommandContext context) {
        String url = "https://nekos.brussell.me/api/v1/images/search";
        JSONObject payload = new JSONObject();
        payload.put("nsfw", true);
        payload.put("limit", 25);
        payload.put("tags", MiscUtils.arrayToString(context.getArgs(), " "));

        getReflex().getHttpClient().post(new AbstractCallback() {
            @Override
            public void response(Response response) throws IOException {
                EmbedBuilder embedBuilder = createEmbed();
                embedBuilder.setTitle("Nekos", "https://nekos.brussell.me");

                JSONObject responseObj = new JSONObject(Objects.requireNonNull(response.body()).string());
                JSONArray images = responseObj.getJSONArray("images");

                if (images.length() < 1) {
                    embedBuilder.setColor(Colours.WARN);
                    embedBuilder.setDescription("No posts were found with your specified tags.");

                    context.getChannel().sendMessage(embedBuilder.build()).queue();
                    return;
                }
                JSONObject image = images.getJSONObject(MiscUtils.getRandom(images.length()));

                embedBuilder.setImage("https://nekos.brussell.me/thumbnail/" + image.getString("id"));
                embedBuilder.setDescription("Click [here](https://nekos.brussell.me/thumbnail/" + image.getString("id") + ") for source");

                context.getChannel().sendMessage(embedBuilder.build()).queue();
            }
        }, url, payload);
    }
}
