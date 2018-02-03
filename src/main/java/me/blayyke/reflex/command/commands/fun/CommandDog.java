package me.blayyke.reflex.command.commands.fun;

import me.blayyke.reflex.Colours;
import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandContext;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;

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
        getReflex().getHttpClient().get(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                context.getChannel().sendMessage(createEmbed(Colours.ERROR).setTitle("Failure").setDescription("Failed to get your random dog image!").build()).queue();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                context.getChannel().sendMessage(createEmbed().setTitle("Dog").setImage("https://random.dog/" + response.body().string()).build()).queue();
            }
        }, "https://random.dog/woof");
    }
}