package me.blayyke.reflex.command.commands.fun;

import me.blayyke.reflex.command.ImageCommand;

public class CommandDog extends ImageCommand {
    public CommandDog() {
        super("dog", "Show a random dog image", null);
    }

    @Override
    public String getSiteUrl() {
        return "https://random.dog";
    }

    @Override
    public String getAPIUrl() {
        return getSiteUrl() + "/woof";
    }

    @Override
    public String handle(String string) {
        return "https://random.dog/" + string;
    }
}