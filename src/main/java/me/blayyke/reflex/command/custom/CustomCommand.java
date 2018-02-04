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

    public CustomCommand(Guild guild, String name) {
        if (guild == null || name == null || name.isEmpty()) throw new IllegalArgumentException("invalid arguments");
        this.guild = guild;
        this.name = name;
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.CUSTOM;
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

    public void setCode(String code) {
        this.code = code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setName(String name) {
        this.name = name;
    }
}