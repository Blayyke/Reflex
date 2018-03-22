package me.blayyke.reflex.command.commands.utilities;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandEnvironment;
import me.blayyke.reflex.utils.AbstractCallback;
import me.blayyke.reflex.utils.MiscUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

public class CommandIMDB extends AbstractCommand {
    public CommandIMDB() {
        super(CommandCategory.UTILITIES, "imdb", "Get movie/show data from IMDB", new String[]{"movie"});
    }

    @Override
    protected void onCommand(CommandEnvironment env) {
        String url = "http://www.omdbapi.com/?t=" + MiscUtils.arrayToString(env.getArgs(), "+") + "&apikey=" + getReflex().getDataManager().getSettings().getOMDBAuth();

        getReflex().getHttpClient().get(new AbstractCallback() {
            @Override
            public void response(Response response) throws IOException {
                JSONObject body = new JSONObject(Objects.requireNonNull(response.body()).string());

                if (!body.getBoolean("Response")) {
                    replyError(env, body.getString("Error"));
                    return;
                }

                String title = body.getString("Title");
                String rating = body.getString("Rated");
                String releaseDate = body.getString("Released");
                String runtime = body.getString("Runtime");
                String genres = body.getString("Genre");
                String plot = body.getString("Plot");
                String language = body.getString("Language");
                String posterImg = body.getString("Poster");
                String metascore = body.getString("Metascore");
                String imdbRating = body.getString("imdbRating");
                String imdbVotes = body.getString("imdbVotes");

                EmbedBuilder embed = createEmbed();

                embed.setTitle(title);
                embed.setImage(posterImg);
                embed.setDescription(plot);

                embed.addField("Release", releaseDate, true);
                embed.addField("Rated", rating, true);

                embed.addField("Metascore", metascore, true);
                embed.addField("Rating", imdbRating, true);

                embed.addField("Votes", imdbVotes, true);
                embed.addField("Runtime", runtime, true);

                embed.addField("Genre(s)", genres, true);
                embed.addField("Language", language, true);

                env.getChannel().sendMessage(embed.build()).queue();
            }
        }, url);
    }

    @Override
    public int getRequiredArgs() {
        return 1;
    }
}