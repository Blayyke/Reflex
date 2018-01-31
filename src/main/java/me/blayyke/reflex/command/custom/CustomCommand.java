package me.blayyke.reflex.command.custom;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandContext;
import net.dv8tion.jda.core.entities.Guild;

import javax.script.ScriptException;

public class CustomCommand extends AbstractCommand {
    private Guild guild;
    private String code;
    private String name;
    private String desc;

    public CustomCommand(String name, String desc, Guild guild, String string) {
        if (guild == null || desc == null || string == null) {
            System.out.println("invalid params!");
            return;
        }
        this.guild = guild;
        this.code = string;
        this.name = name;
        this.desc = desc;
        System.out.println("Command passed!");
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.CUSTOM;
    }

    CustomCommand(Guild guild, String code) {
        this.code = code;
    }

    private String getCommandCode() {
        return code;
    }

    @Override
    public void execute(CommandContext context) {
        try {
            getReflex().getCustomCommandManager().getExecutionEngine().eval(
                    "(function(){"
                            + getCommandCode()
                            + "})();");
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public Guild getGuild() {
        return guild;
    }
}