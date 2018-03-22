package me.blayyke.reflex.command.commands.fun;

import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.ImageCommand;
import org.json.JSONObject;

public class CommandCat extends ImageCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.FUN;
    }

    @Override
    public String getName() {
        return "cat";
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