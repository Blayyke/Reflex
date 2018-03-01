package me.blayyke.reflex.command.commands.utilities;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandEnvironment;
import me.blayyke.reflex.utils.AbstractCallback;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

public class CommandGeoLocate extends AbstractCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.UTILITIES;
    }

    @Override
    public String getName() {
        return "geoip";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"geolocate"};
    }

    @Override
    public String getDesc() {
        return "Shows the rough geographical location of an IP address";
    }

    @Override
    public void onCommand(CommandEnvironment env) {
        String url = "https://freegeoip.net/json/" + env.getArgs()[0];
        getReflex().getHttpClient().get(new AbstractCallback() {
            @Override
            public void response(Response response) throws IOException {
                JSONObject bodyObj = new JSONObject(Objects.requireNonNull(response.body()).string());
                env.getChannel().sendMessage(createEmbed().setTitle("IP Location").setDescription("The rough geographical location for that IP is " + bodyObj.getString("country_name") + "/" + bodyObj.getString("region_name") + "/" + bodyObj.getString("city")).build()).queue();
            }
        }, url);
    }

    @Override
    public int getCooldown() {
        return 3;
    }

    @Override
    public int getRequiredArgs() {
        return 1;
    }
}