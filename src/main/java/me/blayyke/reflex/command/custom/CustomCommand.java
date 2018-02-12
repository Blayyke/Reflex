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
import net.dv8tion.jda.core.entities.Member;

import javax.script.ScriptException;

public class CustomCommand extends AbstractCommand {
    private Guild guild;
    private String action = null;
    private String desc = null;
    private String name;
    private long creatorId;

    private static final String FUNCTION_RANDOM = "function random() {\n\tif(arguments.length===0) return \"\";\nreturn arguments[Math.floor(Math.random() * arguments.length)];\n}";
    private static final String FUNCTION_RANDOM_NUMBER = "function random_number(maxNum) {\n\treturn Math.floor(Math.random() * maxNum) + 1;\n}";
    private CustomCommandType type;

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
        if (getAction().isEmpty()) {
            context.getChannel().sendMessage("Action not set for this command. Please get an admin to set it with " + context.getPrefixUsed() + "custom action " + getName() + " <action>").queue();
            return;
        }

        if (type == CustomCommandType.MESSAGE) {
            context.getChannel().sendMessage(getAction()).queue();
        } else if (type == CustomCommandType.ADVANCED) {
            try {
                String code = FUNCTION_RANDOM + "\n" + FUNCTION_RANDOM_NUMBER + "\n" + getMainFunction(getAction());
                getReflex().getCustomCommandManager().getExecutionEngine().eval(code);
            } catch (ScriptException e) {
                throw new RuntimeException(e);
            }
        } else {
            context.getChannel().sendMessage("Custom command type not set for this command. Please get an admin to set it with " + context.getPrefixUsed() + "custom type " + getName() + " <type>").queue();
        }
    }

    private String getMainFunction(String action) {
        return "(function() {\n\t" +
                getAction() +
                "\n})();\n";
    }

    @Override
    public String getName() {
        return name;
    }

    public void setAction(String action) {
        this.action = action;
        DatabaseUtils.setHashString(getGuild(), getReflex().getDBManager().getSync(), DBEntryKey.CUSTOM_COMMAND.getRedisKey() + "_" + name, DBEntryKeyCCmd.ACTION, action);
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
        DatabaseUtils.setHashNumber(getGuild(), getReflex().getDBManager().getSync(), DBEntryKey.CUSTOM_COMMAND.getRedisKey() + "_" + name, DBEntryKeyCCmd.CREATOR, creatorId);
    }

    public void setDesc(String desc) {
        this.desc = desc;
        DatabaseUtils.setHashString(getGuild(), getReflex().getDBManager().getSync(), DBEntryKey.CUSTOM_COMMAND.getRedisKey() + "_" + name, DBEntryKeyCCmd.DESCRIPTION, desc);
    }

    private String getAction() {
        return action;
    }

    public Member getCreator() {
        return guild.getMemberById(creatorId);
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public Guild getGuild() {
        return guild;
    }

    public CustomCommandType getType() {
        return type;
    }

    public void setType(CustomCommandType type) {
        this.type = type;
        DatabaseUtils.setHashString(getGuild(), getReflex().getDBManager().getSync(), DBEntryKey.CUSTOM_COMMAND.getRedisKey() + "_" + name, DBEntryKeyCCmd.TYPE, type.toString().toLowerCase());
    }
}