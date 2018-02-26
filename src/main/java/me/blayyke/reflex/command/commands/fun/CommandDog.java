package me.blayyke.reflex.command.commands.fun;

import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.ImageCommand;

public class CommandDog extends ImageCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.FUN;
    }

    @Override
    public String getName() {
        return "dog";
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