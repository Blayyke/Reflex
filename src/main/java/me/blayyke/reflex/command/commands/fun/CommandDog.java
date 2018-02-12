package me.blayyke.reflex.command.commands.fun;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandContext;
import me.blayyke.reflex.utils.AbstractCallback;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;

public class CommandDog extends AbstractCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.FUN;
    }

    @Override
    public String getName() {
        return "dog";
    }

    @Override
    public String getDesc() {
        return "Get a random picture of a dog";
    }

    @Override
    public void execute(CommandContext context) {
        getReflex().getHttpClient().get(new AbstractCallback() {
            @Override
            public void response(Response response) throws IOException {
                context.getChannel().sendMessage(createEmbed().setTitle("Dog").setImage("https://random.dog/" + Objects.requireNonNull(response.body()).string()).build()).queue();
            }
        }, "https://random.dog/woof");
    }
}