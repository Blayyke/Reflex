package me.blayyke.reflex.command.commands.fun;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandEnvironment;
import me.blayyke.reflex.utils.AbstractCallback;
import net.dv8tion.jda.core.EmbedBuilder;
import okhttp3.Headers;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

public class CommandDadJoke extends AbstractCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.FUN;
    }

    @Override
    public String getName() {
        return "dadjoke";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"dadjokes"};
    }

    @Override
    public String getDesc() {
        return "Show a random dad joke";
    }

    @Override
    public void onCommand(CommandEnvironment env) {
        String url = "https://icanhazdadjoke.com";

        getReflex().getHttpClient().get(new AbstractCallback() {
            @Override
            public void response(Response response) throws IOException {
                JSONObject object = new JSONObject(Objects.requireNonNull(response.body()).string());
                EmbedBuilder embed = createEmbed();

                embed.setTitle("Dad Joke", "https://icanhazdadjoke.com/j/" + object.getString("id"));
                embed.setDescription(object.getString("joke"));

                env.getChannel().sendMessage(embed.build()).queue();
            }
        }, url, new Headers.Builder().add("Accept", "application/json").build());
    }

    @Override
    public int getCooldown() {
        return 5;
    }
}