package me.blayyke.reflex.command.commands.utilities;

import me.blayyke.reflex.Colours;
import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandContext;
import me.blayyke.reflex.utils.AbstractCallback;
import me.blayyke.reflex.utils.MiscUtils;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class CommandUrban extends AbstractCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.UTILITIES;
    }

    @Override
    public String getName() {
        return "urban";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"ud", "urbandict", "urbandictionary", "dictionary", "define"};
    }

    @Override
    public String getDesc() {
        return "Get the definition of something from UrbanDictionary.com";
    }

    @Override
    public void onCommand(CommandContext context) {
        String query = MiscUtils.arrayToString(context.getArgs(), "+");
        String url = "http://api.urbandictionary.com/v0/define?term=" + query;
        AbstractCallback callback = new AbstractCallback() {
            @Override
            public void response(Response response) throws IOException {
                ResponseBody body = response.body();
                if (body == null) {
                    System.out.println("No body");
                    return;
                }

                JSONObject bodyObj = new JSONObject(body.string());

                String resultType = bodyObj.getString("result_type");
                if (resultType.equalsIgnoreCase("no_results")) {
                    context.getChannel().sendMessage(createEmbed(Colours.WARN)
                            .setTitle("Urban Dictionary")
                            .setDescription("No results were found using your query!")
                            .build()).queue();
                    return;
                }
                JSONArray results = bodyObj.getJSONArray("list");
                JSONObject result = results.getJSONObject(0);
                String example = result.getString("example");
                String definition = result.getString("definition");

                context.getChannel().sendMessage(createEmbed(Colours.INFO)
                        .setTitle("Urban Dictionary")
                        .addField("Definition", definition, false)
                        .addField("Example", example, false)
                        .build()).queue();
            }
        };

        getReflex().getHttpClient().get(callback, url);
    }

    @Override
    public int getCooldown() {
        return 3;
    }
}