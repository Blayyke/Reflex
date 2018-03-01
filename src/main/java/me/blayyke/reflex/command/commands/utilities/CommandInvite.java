package me.blayyke.reflex.command.commands.utilities;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandEnvironment;

public class CommandInvite extends AbstractCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.UTILITIES;
    }

    @Override
    public String getName() {
        return "invite";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"guild", "support"};
    }

    @Override
    public String getDesc() {
        return "Send the invite URL for the support discord and the bot";
    }

    @Override
    public void onCommand(CommandEnvironment env) {
        env.getChannel().sendMessage("**Support guild**: https://discord.gg/h5V3c9s\n" +
                "**Bot invite**: https://reflex-bot.github.io/invite").queue();
    }
}