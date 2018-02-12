package me.blayyke.reflex.command.commands.fun;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandContext;
import me.blayyke.reflex.utils.AbstractCallback;
import net.dv8tion.jda.core.EmbedBuilder;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

public class CommandFortune extends AbstractCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.FUN;
    }

    @Override
    public String getName() {
        return "fortune";
    }

    @Override
    public String getDesc() {
        return "Get a random fortune";
    }

    @Override
    public void execute(CommandContext context) {
        String url = "http://fortunecookieapi.herokuapp.com/v1/cookie";
        getReflex().getHttpClient().get(new AbstractCallback() {
            @Override
            public void response(Response response) throws IOException {
                EmbedBuilder embedBuilder = createEmbed();
                JSONArray cookieArray = new JSONArray(Objects.requireNonNull(response.body()).string());
                JSONObject obj = cookieArray.getJSONObject(0).getJSONObject("fortune");

                embedBuilder.setTitle("Fortune");
                embedBuilder.setDescription(obj.getString("message"));

                context.getChannel().sendMessage(embedBuilder.build()).queue();
            }
        }, url);
    }
}