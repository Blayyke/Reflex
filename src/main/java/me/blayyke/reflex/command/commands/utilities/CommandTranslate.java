package me.blayyke.reflex.command.commands.utilities;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandEnvironment;
import me.blayyke.reflex.utils.AbstractCallback;
import me.blayyke.reflex.utils.MiscUtils;
import me.blayyke.reflex.utils.TextUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import okhttp3.Response;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.TreeBidiMap;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

public class CommandTranslate extends AbstractCommand {
    private BidiMap<String, String> langMap = new TreeBidiMap<>();

    public CommandTranslate() {
        super(CommandCategory.UTILITIES, "translate", "Translate text using yandex.net translator", null);
    }

    @Override
    protected void init() {
        String url = "https://translate.yandex.net/api/v1.5/tr.json/getLangs" +
                "?ui=en" +
                "&key=" + getReflex().getDataManager().getSettings().getYandexAuth();

        getReflex().getHttpClient().get(new AbstractCallback() {
            @Override
            public void response(Response response) throws IOException {
                JSONObject obj = new JSONObject(Objects.requireNonNull(response.body()).string());
                JSONObject langs = obj.getJSONObject("langs");
                for (String s : langs.keySet()) {
                    langMap.put(s.toLowerCase(), langs.getString(s).toLowerCase());
                }
            }
        }, url);
    }

    @Override
    protected void onCommand(CommandEnvironment env) {
        String lang = env.getArgs()[0];
        if (!langMap.containsKey(lang.toLowerCase())) {
            if (langMap.containsValue(lang.toLowerCase())) {
                lang = langMap.getKey(lang.toLowerCase());
            } else {
                replyError(env, "That language is not supported!");
                return;
            }
        }
        String url = "https://translate.yandex.net/api/v1.5/tr.json/translate" +
                "?key=" + getReflex().getDataManager().getSettings().getYandexAuth() + "" +
                "&text=" + MiscUtils.arrayToString(env.getArgs(), 1, "%20") +
                "&lang=" + lang;

        getReflex().getHttpClient().get(new AbstractCallback() {
            @Override
            public void response(Response response) throws IOException {
                JSONObject obj = new JSONObject(Objects.requireNonNull(response.body()).string());
                if (obj.getInt("code") != 200) throw new RuntimeException("invalid code " + obj.getInt("code"));

                String[] lang = obj.getString("lang").split("-");
                EmbedBuilder embedBuilder = createEmbed();
                embedBuilder.setTitle("Translate");

                embedBuilder.addField("Text", MiscUtils.arrayToString(env.getArgs(), 1, " "), false);
                embedBuilder.addField("Translated Text", obj.getJSONArray("text").getString(0), false);
                embedBuilder.addBlankField(false);
                embedBuilder.addField("Language from", TextUtils.uppercaseFirst(langMap.get(lang[0])), true);
                embedBuilder.addField("Language to", TextUtils.uppercaseFirst(langMap.get(lang[1])), true);
                env.getChannel().sendMessage(embedBuilder.build()).queue();
            }
        }, url);
    }

    @Override
    public int getCooldown() {
        return 5;
    }

    @Override
    public int getRequiredArgs() {
        return 2;
    }
}