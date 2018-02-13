package me.blayyke.reflex;

import me.blayyke.reflex.utils.AbstractCallback;
import net.dv8tion.jda.core.JDA;
import okhttp3.Headers;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

public class BotStatsPoster {
    private Reflex reflex;

    public BotStatsPoster(Reflex reflex) {
        this.reflex = reflex;
    }

    public void updateAllStats(JDA shard) {
        updateStats(shard, "https://bots.discord.pw/api/bots/" + shard.getSelfUser().getId() + "/stats", reflex.getSettings().getDpwAuth());
    }

    private void updateStats(JDA shard, String url, String auth) {
        JSONObject postBody = new JSONObject();
        postBody.put("server_count", reflex.getShardManager().getGuilds().size());
        postBody.put("shard_id", shard.getShardInfo().getShardId());
        postBody.put("shard_count", reflex.getShardManager().getShardsTotal());
        doPost(url, postBody, auth);
    }

    private void doPost(String url, JSONObject postBody, String auth) {
        Headers headers = new Headers.Builder().add("Authorization", auth).build();

        reflex.getHttpClient().post(new AbstractCallback() {
            @Override
            public void response(Response response) throws IOException {
                System.out.println(Objects.requireNonNull(response.body()).string());
                reflex.getLogger().info("Updated bot stats for " + url + (postBody.has("shard_id") ? "on shard " + postBody.getInt("shard_id") : "") + ".");
            }
        }, url, postBody, headers);
    }
}