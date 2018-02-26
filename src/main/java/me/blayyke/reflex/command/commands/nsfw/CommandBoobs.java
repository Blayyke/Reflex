package me.blayyke.reflex.command.commands.nsfw;

import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.ImageCommand;
import org.json.JSONArray;

public class CommandBoobs extends ImageCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.NSFW;
    }

    @Override
    public String getName() {
        return "boobs";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"tits", "nude"};
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