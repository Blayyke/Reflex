package me.blayyke.reflex.command.custom;

import me.blayyke.reflex.Reflex;
import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandContext;
import me.blayyke.reflex.database.DBEntryKey;
import me.blayyke.reflex.database.DBEntryKeyCCmd;
import me.blayyke.reflex.utils.DatabaseUtils;
import me.blayyke.reflex.utils.MiscUtils;
import net.dv8tion.jda.core.entities.Guild;

import javax.script.ScriptException;

public class CustomCommand extends AbstractCommand {
    private Guild guild;
    private String action = null;
    private String desc = null;
    private String name;

    public CustomCommand(Reflex reflex, Guild guild, String name) {
        if (guild == null || name == null || name.isEmpty()) throw new IllegalArgumentException("invalid arguments");
        if (!MiscUtils.isAllLetters(name))
            throw new IllegalArgumentException("name cannot contain non-alphabetic characters");
        this.guild = guild;
        this.name = name;
        this.setReflex(reflex);
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.CUSTOM;
    }

    @Override
    public void execute(CommandContext context) {
        try {
            getReflex().getCustomCommandManager().getExecutionEngine().eval(
                    "(function(){"
                            + getAction()
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

    public void setAction(String action) {
        this.action = action;
        DatabaseUtils.setHashString(getGuild(), getReflex().getDBManager().getSync(), DBEntryKey.CUSTOM_COMMAND.getRedisKey() + "_" + name, DBEntryKeyCCmd.ACTION, action);
    }

    public void setDesc(String desc) {
        this.desc = desc;
        DatabaseUtils.setHashString(getGuild(), getReflex().getDBManager().getSync(), DBEntryKey.CUSTOM_COMMAND.getRedisKey() + "_" + name, DBEntryKeyCCmd.DESCRIPTION, desc);
    }

    private String getAction() {
        return action;
    }
}