package me.blayyke.reflex.command.commands.nsfw;

import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.ImageCommand;
import org.json.JSONArray;

public class CommandBoobs extends ImageCommand {
    public CommandBoobs() {
        super(CommandCategory.NSFW, "boobs", "Send an NSFW image", new String[]{"tits", "nude"});
    }

    @Override
    public String getAPIUrl() {
        return "http://api.oboobs.ru/noise/1/";
    }

    @Override
    public String getSiteUrl() {
        return "http://oboobs.ru";
    }

    @Override
    public String handle(String string) {
        return "http://media.oboobs.ru/" + new JSONArray(string).getJSONObject(0).getString("preview");
    }
}