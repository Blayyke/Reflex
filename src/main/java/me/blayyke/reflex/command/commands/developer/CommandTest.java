package me.blayyke.reflex.command.commands.developer;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandContext;

public class CommandTest extends AbstractCommand {
    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getDesc() {
        return "Test cmd";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.DEVELOPER;
    }

    @Override
    public void execute(CommandContext context) {
    }
}