package me.blayyke.reflex.command.commands.utilities;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandEnvironment;
import me.blayyke.reflex.utils.MiscUtils;
import me.blayyke.reflex.utils.UserUtils;

public class CommandSay extends AbstractCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.UTILITIES;
    }

    @Override
    public String getName() {
        return "say";
    }

    @Override
    public String getDesc() {
        return "Make the bot say something";
    }

    @Override
    protected void onCommand(CommandEnvironment env) {
        env.getChannel().sendMessage("**" + UserUtils.formatUser(env.getMember().getUser()) + "** - " + MiscUtils.arrayToString(env.getArgs(), " ")).queue();
    }

    @Override
    public int getRequiredArgs() {
        return 1;
    }
}