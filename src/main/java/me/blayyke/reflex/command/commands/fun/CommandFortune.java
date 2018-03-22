package me.blayyke.reflex.command.commands.fun;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandEnvironment;
import me.blayyke.reflex.utils.AbstractCallback;
import net.dv8tion.jda.core.EmbedBuilder;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

public class CommandFortune extends AbstractCommand {
    public CommandFortune() {
        super(CommandCategory.FUN, "fortune", "Get a random fortune", null);
    }

    @Override
    public void onCommand(CommandEnvironment env) {
        String url = "http://fortunecookieapi.herokuapp.com/v1/cookie";
        getReflex().getHttpClient().get(new AbstractCallback() {
            @Override
            public void response(Response response) throws IOException {
                EmbedBuilder embedBuilder = createEmbed();
                JSONArray array = new JSONArray(Objects.requireNonNull(response.body()).string());
                JSONObject object = array.getJSONObject(0).getJSONObject("fortune");

                embedBuilder.setTitle("Fortune");
                embedBuilder.setDescription(object.getString("message"));

                env.getChannel().sendMessage(embedBuilder.build()).queue();
            }
        }, url);
    }

    @Override
    public int getCooldown() {
        return 5;
    }
}