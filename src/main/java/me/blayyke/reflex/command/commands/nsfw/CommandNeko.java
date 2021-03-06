package me.blayyke.reflex.command.commands.nsfw;

import me.blayyke.reflex.Colours;
import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandEnvironment;
import me.blayyke.reflex.utils.AbstractCallback;
import me.blayyke.reflex.utils.MiscUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

public class CommandNeko extends AbstractCommand {
    public CommandNeko() {
        super(CommandCategory.NSFW, "neko", "Send an NSFW image", new String[]{"catgirl"});
    }

    @Override
    public void onCommand(CommandEnvironment env) {
        String url = "https://nekos.brussell.me/api/v1/images/search";
        JSONObject payload = new JSONObject();
        payload.put("nsfw", true);
        payload.put("limit", 25);
        payload.put("tags", MiscUtils.arrayToString(env.getArgs(), " "));

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

                    env.getChannel().sendMessage(embedBuilder.build()).queue();
                    return;
                }
                JSONObject image = images.getJSONObject(MiscUtils.getRandom(images.length()));

                embedBuilder.setImage("https://nekos.brussell.me/thumbnail/" + image.getString("id"));
                embedBuilder.setDescription("Click [here](https://nekos.brussell.me/thumbnail/" + image.getString("id") + ") for source");

                env.getChannel().sendMessage(embedBuilder.build()).queue();
            }
        }, url, payload);
    }

    @Override
    public int getCooldown() {
        return 3;
    }
}