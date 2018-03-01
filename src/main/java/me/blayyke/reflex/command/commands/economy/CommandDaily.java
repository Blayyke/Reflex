package me.blayyke.reflex.command.commands.economy;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandEnvironment;
import me.blayyke.reflex.database.keys.user.KeyPointsCooldown;

import java.util.concurrent.TimeUnit;

public class CommandDaily extends AbstractCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.FUN;
    }

    @Override
    public String getName() {
        return "daily";
    }

    @Override
    public String getDesc() {
        return "Get 100 credits for the day";
    }

    @Override
    public void onCommand(CommandEnvironment env) {
        if (getReflex().getDBManager().keyExists(new KeyPointsCooldown(env.getMember().getUser()))) {
            env.getChannel().sendMessage("You have already gotten your daily points!").queue();
            return;
        }

        getReflex().getPointManager().addPoints(env.getMember().getUser(), 100);

        getReflex().getDBManager().set(new KeyPointsCooldown(env.getMember().getUser()), String.valueOf(true));
        getReflex().getDBManager().setExpiry(new KeyPointsCooldown(env.getMember().getUser()), TimeUnit.DAYS, 1);

        env.getChannel().sendMessage("You have been given 100 points! Do this again in 24 hours for more points.").queue();
    }
}