package me.blayyke.reflex;

import me.blayyke.reflex.utils.AbstractCallback;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;

public class HttpClient {
    private final MediaType jsonMediaType = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient client;

    HttpClient() {
        client = new OkHttpClient();
    }

    public void get(AbstractCallback callback, String url) {
        client.newCall(new Request.Builder().url(url).build()).enqueue(callback);
    }

    public Response getSync(String url) {
        return getSync(url, new Headers.Builder().build());
    }

    public Response getSync(String url, Headers headers) {
        try {
            return client.newCall(new Request.Builder().url(url).headers(headers).build()).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void post(AbstractCallback callback, String url, JSONObject body, Headers headers) {
        client.newCall(new Request.Builder().headers(headers).post(RequestBody.create(jsonMediaType, body.toString())).url(url).build()).enqueue(callback);
    }

    public void post(AbstractCallback callback, String url, JSONObject body) {
        post(callback, url, body, new Headers.Builder().build());
    }
}