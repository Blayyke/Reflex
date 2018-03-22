package me.blayyke.reflex.command.commands.fun;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandEnvironment;
import me.blayyke.reflex.utils.MiscUtils;
import me.blayyke.reflex.utils.TextUtils;

public class CommandCursive extends AbstractCommand {
    private CommandCursive() {
        super(CommandCategory.FUN, "cursive", "Turn your text cursive", new String[]{"fancytext"});
    }

    @Override
    protected void onCommand(CommandEnvironment env) {
        env.getChannel().sendMessage(TextUtils.toCursive(MiscUtils.arrayToString(env.getArgs(), " "))).queue();
    }

    @Override
    public int getRequiredArgs() {
        return 1;
    }
}