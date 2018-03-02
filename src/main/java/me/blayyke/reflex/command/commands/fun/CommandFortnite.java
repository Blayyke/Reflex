package me.blayyke.reflex.command.commands.fun;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandEnvironment;
import me.blayyke.reflex.utils.AbstractCallback;
import me.blayyke.reflex.utils.MiscUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import okhttp3.Headers;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class CommandFortnite extends AbstractCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.FUN;
    }

    @Override
    public String getName() {
        return "fortnite";
    }

    @Override
    public String getDesc() {
        return "Show PUBG info for a user";
    }

    private enum FortniteGameMode {
        SOLO("p2"),
        DUOS("p10"),
        SQUADS("p9");

        private final String key;

        FortniteGameMode(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        public static FortniteGameMode getMode(String key) {
            return Arrays.stream(values()).filter(v -> v.toString().equalsIgnoreCase(key)).findFirst().orElse(null);
        }
    }

    @Override
    protected void onCommand(CommandEnvironment env) {
        String platform = env.getArgs()[0];
        FortniteGameMode mode = FortniteGameMode.getMode(env.getArgs()[1]);

        if (mode == null) {
            replyError(env, "Invalid game mode. Valid game modes: " + MiscUtils.arrayToString(FortniteGameMode.values(), ", "));
            return;
        }

        String url = "https://api.fortnitetracker.com/v1/profile/" + platform + "/" + MiscUtils.arrayToString(env.getArgs(), 2, "+");
        Headers headers = new Headers.Builder().add("TRN-Api-Key", getReflex().getDataManager().getSettings().getTrnAuth()).build();

        getReflex().getHttpClient().get(new AbstractCallback() {
            @Override
            public void response(Response response) throws IOException {
                JSONObject object = new JSONObject(Objects.requireNonNull(response.body()).string());
                if (object.has("error")) {
                    replyError(env, object.getString("error"));
                    return;
                }
                JSONObject stats = object.getJSONObject("stats").getJSONObject(mode.getKey());
                EmbedBuilder embed = createEmbed();

                String platformName = object.getString("platformNameLong");
                String username = object.getString("epicUserHandle");

                JSONObject top25 = stats.getJSONObject("top25");
                JSONObject top12 = stats.getJSONObject("top12");
                JSONObject top10 = stats.getJSONObject("top10");
                JSONObject top6 = stats.getJSONObject("top6");
                JSONObject top5 = stats.getJSONObject("top5");
                JSONObject top3 = stats.getJSONObject("top3");
                JSONObject wins = stats.getJSONObject("top1");

                JSONObject kills = stats.getJSONObject("kills");
                JSONObject kd = stats.getJSONObject("kd");
                JSONObject minutesPlayed = stats.getJSONObject("minutesPlayed");
                JSONObject avgTimePlayed = stats.getJSONObject("avgTimePlayed");
                JSONObject score = stats.getJSONObject("score");
                JSONObject scorePerMin = stats.getJSONObject("scorePerMin");
                JSONObject kpg = stats.getJSONObject("kpg");
                JSONObject kpm = stats.getJSONObject("kpm");
                JSONObject scorePerMatch = stats.getJSONObject("scorePerMatch");
                JSONObject matches = stats.getJSONObject("matches");

                embed.setTitle("Fortnite");

                embed.addField("Username", username, true);
                embed.addField("Platform", platformName, true);
                embed.addField(matches.getString("label"), matches.getString("displayValue"), true);

                embed.addField(avgTimePlayed.getString("label"), avgTimePlayed.getString("displayValue"), true);
                embed.addField(minutesPlayed.getString("label"), minutesPlayed.getString("displayValue").trim(), true);
                embed.addField(kd.getString("label"), kd.getString("displayValue"), true);

                embed.addField(scorePerMin.getString("label"), scorePerMin.getString("displayValue"), true);
                embed.addField(scorePerMatch.getString("label"), scorePerMatch.getString("displayValue"), true);
                embed.addField(score.getString("label"), score.getString("displayValue"), true);

                embed.addField(kpm.getString("label"), kpm.getString("displayValue"), true);
                embed.addField(kpg.getString("label"), kpg.getString("displayValue"), true);
                embed.addField(kills.getString("label"), kills.getString("displayValue"), true);


                embed.addField(wins.getString("label"), wins.getString("displayValue"), true);
                embed.addField(top3.getString("label"), top3.getString("displayValue"), true);
                embed.addField(top5.getString("label"), top5.getString("displayValue"), true);

                embed.addField(top10.getString("label"), top10.getString("displayValue"), true);
                embed.addBlankField(true);
                embed.addField(top25.getString("label"), top25.getString("displayValue"), true);

                env.getChannel().sendMessage(embed.build()).queue();
            }
        }, url, headers);
    }

    @Override
    public int getRequiredArgs() {
        return 3;
    }

    @Override
    public int getCooldown() {
        return 5;
    }
}