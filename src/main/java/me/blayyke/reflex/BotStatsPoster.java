package me.blayyke.reflex;

import me.blayyke.reflex.utils.AbstractCallback;
import me.blayyke.reflex.utils.MiscUtils;
import net.dv8tion.jda.core.JDA;
import okhttp3.Headers;
import okhttp3.Response;
import org.json.JSONObject;

public class BotStatsPoster {
    private Reflex reflex;

    public BotStatsPoster(Reflex reflex) {
        this.reflex = reflex;
    }

    public void updateAllStats(JDA shard) {
        updateStats(shard, "https://bots.discord.pw/api/bots/" + shard.getSelfUser().getId() + "/stats", reflex.getDataManager().getSettings().getDpwAuth());
        updateStats(shard, "https://discordbots.org/api/bots/" + shard.getSelfUser().getId() + "/stats", reflex.getDataManager().getSettings().getDboAuth());
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
            public void response(Response response) {
                if (!MiscUtils.equalsAny(response.code(), 200, 204))
                    reflex.getLogger().info("Unexpected response code ({}) received for guild update on {}", response.code(), url);
                else
                    reflex.getLogger().info("Updated bot stats for {} on shard {}.", url, postBody.getInt("shard_id"));

                response.close();
            }
        }, url, postBody, headers);
    }
}