package me.blayyke.reflex.command.commands.utilities;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandEnvironment;
import me.blayyke.reflex.utils.MiscUtils;
import me.blayyke.reflex.utils.UserUtils;

public class CommandSay extends AbstractCommand {
    public CommandSay() {
        super(CommandCategory.UTILITIES, "say", "Say something using the bot", new String[]{"talk", "speak"});
    }

    protected void onCommand(CommandEnvironment env) {
        env.getChannel().sendMessage("**" + UserUtils.formatUser(env.getMember().getUser()) + "** - " + MiscUtils.arrayToString(env.getArgs(), " ")).queue();
    }

    @Override
    public int getRequiredArgs() {
        return 1;
    }

    @Override
    public int getCooldown() {
        return 3;
    }
}