package me.blayyke.reflex;

import okhttp3.*;
import org.json.JSONObject;

public class HttpClient {
    private final MediaType jsonMediaType = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient client;

    HttpClient() {
        client = new OkHttpClient();
    }

    public void get(Callback callback, String url) {
        client.newCall(new Request.Builder().url(url).build()).enqueue(callback);
    }

    public void post(Callback callback, String url, JSONObject body) {
        client.newCall(new Request.Builder().post(RequestBody.create(jsonMediaType, body.toString())).url(url).build()).enqueue(callback);
    }
}