package me.blayyke.reflex.command.commands.fun;

import me.blayyke.reflex.command.ImageCommand;
import org.json.JSONObject;

public class CommandCat extends ImageCommand {
    public CommandCat() {
        super("cat", "Get a random cat picture", new String[]{"kitty"});
    }

    @Override
    public String getAPIUrl() {
        return getSiteUrl() + "/meow";
    }

    @Override
    public String getSiteUrl() {
        return "https://aws.random.cat";
    }

    @Override
    public String handle(String string) {
        return new JSONObject(string).getString("file");
    }
}