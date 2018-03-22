package me.blayyke.reflex.command.commands.economy;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandEnvironment;

public class CommandPoints extends AbstractCommand {
    public CommandPoints() {
        super(CommandCategory.ECONOMY, "points", "View your banked points", new String[]{"credits"});
    }

    @Override
    public void onCommand(CommandEnvironment env) {
        long hashNumber = getReflex().getPointManager().getPoints(env.getMember().getUser());
        env.getChannel().sendMessage("You have " + hashNumber + " points.").queue();
    }
}