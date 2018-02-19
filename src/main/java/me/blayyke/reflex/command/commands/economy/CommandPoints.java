package me.blayyke.reflex.command.commands.economy;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandContext;

public class CommandPoints extends AbstractCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.FUN;
    }

    @Override
    public String getName() {
        return "points";
    }

    @Override
    public String getDesc() {
        return "See how many points you have accumulated";
    }

    @Override
    public void execute(CommandContext context) {
        long hashNumber = getReflex().getPointManager().getPoints(context.getMember().getUser());
        context.getChannel().sendMessage("You have " + hashNumber + " points.").queue();
    }
}