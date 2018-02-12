package me.blayyke.reflex.utils;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;

public abstract class AbstractCallback implements Callback {
    @Override
    @ParametersAreNonnullByDefault
    public void onFailure(Call call, IOException e) {
        this.failure(e);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onResponse(Call call, Response response) throws IOException {
        this.response(response);
    }

    public void failure(IOException e) {
    }

    public abstract void response(Response response) throws IOException;
}