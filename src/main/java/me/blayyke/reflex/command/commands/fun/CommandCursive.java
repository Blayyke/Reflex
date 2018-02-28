package me.blayyke.reflex.command.commands.fun;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandContext;
import me.blayyke.reflex.utils.MiscUtils;
import me.blayyke.reflex.utils.TextUtils;

public class CommandCursive extends AbstractCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.FUN;
    }

    @Override
    public String getName() {
        return "cursive";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"fancy"};
    }

    @Override
    public String getDesc() {
        return "Turn your text into cursive";
    }

    @Override
    protected void onCommand(CommandContext context) {
        context.getChannel().sendMessage(TextUtils.toCursive(MiscUtils.arrayToString(context.getArgs(), " "))).queue();
    }

    @Override
    public int getRequiredArgs() {
        return 1;
    }
}