package me.blayyke.reflex.command;

import me.blayyke.reflex.utils.AbstractCallback;
import me.blayyke.reflex.utils.TextUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;

public abstract class ImageCommand extends AbstractCommand {
    public abstract String getAPIUrl();

    public abstract String getSiteUrl();

    public abstract String handle(String string);

    private interface Test {
        void receive(String receive);
    }

    @Override
    public final String getDesc() {
        return "Get a random image from " + getSiteUrl();
    }

    @Override
    public final void onCommand(CommandEnvironment env) {
        EmbedBuilder embedBuilder = createEmbed();

        embedBuilder.setTitle(TextUtils.uppercaseFirst(getName()), getSiteUrl());
        get(receive -> {
            embedBuilder.setImage(receive);
            env.getChannel().sendMessage(embedBuilder.build()).queue();
        });
    }

    private void get(Test test) {
        getReflex().getHttpClient().get(new AbstractCallback() {
            @Override
            public void response(Response response) throws IOException {
                test.receive(handle(Objects.requireNonNull(response.body()).string()));
                response.close();
            }
        }, getAPIUrl());
    }

    @Override
    public int getCooldown() {
        return 3;
    }
}