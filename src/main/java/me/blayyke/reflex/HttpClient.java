package me.blayyke.reflex;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpClient {
    private final OkHttpClient client;

    HttpClient() {
        client = new OkHttpClient();
    }

    public void get(Callback callback, String url) {
        client.newCall(new Request.Builder().url(url).build()).enqueue(callback);
    }
}