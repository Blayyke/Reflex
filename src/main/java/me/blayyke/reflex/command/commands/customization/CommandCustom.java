package me.blayyke.reflex.command.commands.customization;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandContext;
import me.blayyke.reflex.command.custom.CustomCommand;
import me.blayyke.reflex.utils.MiscUtils;

public class CommandCustom extends AbstractCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.DEVELOPER;
    }

    @Override
    public String getName() {
        return "custom";
    }

    @Override
    public String getDesc() {
        return "Create, delete and manage custom commands for this guild.";
    }

    @Override
    public void execute(CommandContext context) {
        switch (context.getArgs()[0].toLowerCase()) {
            case "create":
                break;
            case "name":
                break;
            default:
                break;
        }

        CustomCommand command = new CustomCommand(context.getArgs()[0], "AA", context.getGuild(), MiscUtils.arrayToString(context.getArgs(), 1, " "));
        getReflex().getCustomCommandManager().createCommand(command);
        context.getChannel().sendMessage("Created custom command " + command.getName()).queue();
    }
}