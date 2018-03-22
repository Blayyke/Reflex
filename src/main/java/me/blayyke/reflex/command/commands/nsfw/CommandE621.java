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

public class CommandE621 extends AbstractCommand {
    public CommandE621() {
        super(CommandCategory.NSFW, "e621", "Send an NSFW image", new String[]{"furry"});
    }

    @Override
    public void onCommand(CommandEnvironment env) {
        String url = "https://e621.net/post/index.json?tags=" + MiscUtils.arrayToString(env.getArgs(), "_");
        getReflex().getHttpClient().get(new AbstractCallback() {
            @Override
            public void response(Response response) throws IOException {
                JSONArray posts = new JSONArray(Objects.requireNonNull(response.body()).string());
                EmbedBuilder embedBuilder = createEmbed();
                embedBuilder.setTitle("E621", "https://e621.net");

                if (posts.length() < 1) {
                    embedBuilder.setColor(Colours.WARN);
                    embedBuilder.setDescription("No posts were found with your specified tags.");

                    env.getChannel().sendMessage(embedBuilder.build()).queue();
                    return;
                }

                JSONObject jsonObject = posts.getJSONObject(MiscUtils.getRandom(posts.length()));

                embedBuilder.setImage(jsonObject.getString("file_url"));
                embedBuilder.setDescription("Click [here](" + jsonObject.getString("source") + ") for source");

                env.getChannel().sendMessage(embedBuilder.build()).queue();
            }
        }, url);
    }
}