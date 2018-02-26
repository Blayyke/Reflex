package me.blayyke.reflex.utils;

import me.blayyke.reflex.Reflex;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;

public abstract class AbstractCallback implements Callback {
    @Override
    @ParametersAreNonnullByDefault
    public void onFailure(Call call, IOException e) {
        this.failure(e);
        LoggerFactory.getLogger(Reflex.class.getSimpleName()).warn("Got a failure on {}: {}", call.request().url().url().toString(), e.getMessage());
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