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

public class CommandLoli extends AbstractCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.NSFW;
    }

    @Override
    public String getName() {
        return "loli";
    }

    @Override
    public String getDesc() {
        return null;
    }

    @Override
    public void execute(CommandContext context) {
        String url = "https://lolibooru.moe/post.json?tags=" + MiscUtils.arrayToString(context.getArgs(), "_");
        getReflex().getHttpClient().get(new AbstractCallback() {
            @Override
            public void response(Response response) throws IOException {
                JSONArray posts = new JSONArray(Objects.requireNonNull(response.body()).string());
                EmbedBuilder embedBuilder = createEmbed();
                embedBuilder.setTitle("Loli", "https://lolibooru.moe");

                if (posts.length() < 1) {
                    embedBuilder.setColor(Colours.WARN);
                    embedBuilder.setDescription("No posts were found with your specified tags.");

                    context.getChannel().sendMessage(embedBuilder.build()).queue();
                    return;
                }

                JSONObject jsonObject = posts.getJSONObject(MiscUtils.getRandom(posts.length()));

                embedBuilder.setImage(jsonObject.getString("preview_url"));
                embedBuilder.setDescription("Click [here](" + jsonObject.getString("source") + ") for source");

                context.getChannel().sendMessage(embedBuilder.build()).queue();
            }
        }, url);
    }
}