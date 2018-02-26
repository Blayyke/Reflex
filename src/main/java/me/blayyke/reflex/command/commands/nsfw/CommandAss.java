package me.blayyke.reflex.command.commands.nsfw;

import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.ImageCommand;
import org.json.JSONArray;

public class CommandAss extends ImageCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.NSFW;
    }

    @Override
    public String getName() {
        return "butts";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"ass", "pussy", "butt"};
    }

    @Override
    public String getAPIUrl() {
        return "http://api.obutts.ru/noise/1/";
    }

    @Override
    public String getSiteUrl() {
        return "http://obutts.ru";
    }

    @Override
    public String handle(String string) {
        return "http://media.obutts.ru/" + new JSONArray(string).getJSONObject(0).getString("preview");
    }
}