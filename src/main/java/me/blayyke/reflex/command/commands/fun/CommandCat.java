package me.blayyke.reflex.command.commands.fun;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandContext;
import me.blayyke.reflex.utils.AbstractCallback;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

public class CommandCat extends AbstractCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.FUN;
    }

    @Override
    public String getName() {
        return "cat";
    }

    @Override
    public String getDesc() {
        return "Get a random picture of a cat";
    }

    @Override
    public void execute(CommandContext context) {
        getReflex().getHttpClient().get(new AbstractCallback() {
            @Override
            public void response(Response response) throws IOException {
                context.getChannel().sendMessage(createEmbed().setTitle("Cat").setImage(new JSONObject(Objects.requireNonNull(response.body()).string()).getString("file")).build()).queue();
            }
        }, "https://random.cat/meow");
    }
}