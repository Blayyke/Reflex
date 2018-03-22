package me.blayyke.reflex.command.commands.fun;

import me.blayyke.reflex.command.ImageCommand;

public class CommandBirb extends ImageCommand {
    public CommandBirb() {
        super("birb", "Get a random bird image", new String[]{"bird"});
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