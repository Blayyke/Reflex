package me.blayyke.reflex.command.commands.fun;

import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.ImageCommand;

public class CommandBirb extends ImageCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.FUN;
    }

    @Override
    public String getName() {
        return "birb";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"bird"};
    }

    @Override
    public String getAPIUrl() {
        return "https://random.birb.pw/tweet";
    }

    @Override
    public String getSiteUrl() {
        return "https://random.birb.pw";
    }

    @Override
    public String handle(String string) {
        return "https://random.birb.pw/img/" + string;
    }
}