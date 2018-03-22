package me.blayyke.reflex.command.commands.utilities;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandEnvironment;

public class CommandInvite extends AbstractCommand {
    public CommandInvite() {
        super(CommandCategory.UTILITIES, "invite", "Invite me to your server or join mine", new String[]{"support", "addme", "inviteme"});
    }

    @Override
    public void onCommand(CommandEnvironment env) {
        env.getChannel().sendMessage("**Support guild**: https://discord.gg/h5V3c9s\n" +
                "**Bot invite**: https://reflex-bot.github.io/invite").queue();
    }
}