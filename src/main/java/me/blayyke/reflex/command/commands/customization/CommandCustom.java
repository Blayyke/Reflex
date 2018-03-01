package me.blayyke.reflex.command.commands.customization;

import me.blayyke.reflex.Colours;
import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandEnvironment;
import me.blayyke.reflex.command.custom.CustomCommand;
import me.blayyke.reflex.command.custom.CustomCommandType;
import me.blayyke.reflex.utils.MiscUtils;
import me.blayyke.reflex.utils.TextUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;

import java.util.Arrays;

public class CommandCustom extends AbstractCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.CUSTOMIZATION;
    }

    @Override
    public Permission[] getRequiredPermissions() {
        return new Permission[]{Permission.MANAGE_SERVER};
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
    public void onCommand(CommandEnvironment env) {
        EmbedBuilder embed = createEmbed();

        switch (env.getArgs()[0].toLowerCase()) {
            case "create":
                if (getReflex().getCustomCommandManager().commandExists(env.getGuild(), env.getArgs()[1])) {
                    replyError(env, "A command already exists with that name. If you would like to delete this command, use `" + env.getPrefixUsed() + env.getAlias() + " delete <name>.");
                    return;
                }
                if (getReflex().getCommandManager().getCommand(env.getArgs()[1]) != null) {
                    replyError(env, "A default command already exists with that name. Please use another name.");
                    return;
                }

                CustomCommand command = new CustomCommand(getReflex(), env.getGuild(), env.getArgs()[1]);
                command.setAction(null);
                command.setCreatorId(env.getMember().getUser().getIdLong());
                getReflex().getCustomCommandManager().createCommand(command);
                env.getChannel().sendMessage(embed.setTitle("Created command").setDescription("Successfully created command `" + command.getName() + "`.").build()).queue();
                break;
            case "action": {
                String s = env.getArgs()[1];
                CustomCommand c = getReflex().getCustomCommandManager().getCommand(env.getGuild(), s);
                if (c == null) {
                    commandNotFound(env);
                    return;
                }

                c.setAction(MiscUtils.arrayToString(env.getArgs(), 2, " "));
                embed.setTitle("Command updated").setDescription("The action for that command has been updated.");
                env.getChannel().sendMessage(embed.build()).queue();
                break;
            }
            case "description":
            case "desc": {
                String s = env.getArgs()[1];
                CustomCommand c = getReflex().getCustomCommandManager().getCommand(env.getGuild(), s);
                if (c == null) {
                    commandNotFound(env);
                    return;
                }

                c.setDesc(MiscUtils.arrayToString(env.getArgs(), 2, " "));
                env.getChannel().sendMessage(embed.setTitle("Command updated").setDescription("Changed description of " + c.getName() + " to `" + c.getDesc() + "`.").build()).queue();
                break;
            }
            case "delete": {
                String s = env.getArgs()[1];
                CustomCommand c = getReflex().getCustomCommandManager().getCommand(env.getGuild(), s);
                if (c == null) {
                    commandNotFound(env);
                    return;
                }

                getReflex().getCustomCommandManager().deleteCommand(c);
                embed.setTitle("Command deleted");
                embed.setDescription("Command `" + c.getName() + "` has successfully been deleted.");
                env.getChannel().sendMessage(embed.build()).queue();
                break;
            }
            case "mode":
            case "type": {
                String s = env.getArgs()[1];
                CustomCommand c = getReflex().getCustomCommandManager().getCommand(env.getGuild(), s);
                if (c == null) {
                    commandNotFound(env);
                    return;
                }

                CustomCommandType customCommandType = Arrays.stream(CustomCommandType.values()).filter(type -> type.toString().equalsIgnoreCase(env.getArgs()[2])).findAny().orElse(null);

                if (customCommandType == null) {
                    embed.setTitle("Invalid type");
                    replyError(env, "`" + TextUtils.escapeFormatting(env.getArgs()[2]) + "` is not a valid command type. Valid types are: " + MiscUtils.arrayToString(CustomCommandType.values(), ", "));

                    env.getChannel().sendMessage(embed.build()).queue();
                    return;
                }

                c.setType(customCommandType);
                embed.setTitle("Command type updated");
                embed.setDescription("Changed command type of " + c.getName() + " to " + c.getType().toString() + ".");

                env.getChannel().sendMessage(embed.build()).queue();
                break;
            }
            default:
                embed.setTitle("Unknown argument(s)");
                embed.setDescription("Please provide a valid method argument. \nValid arguments include: ");

                env.getChannel().sendMessage(embed.build()).queue();
                break;
        }

    }

    @Override
    public int getRequiredArgs() {
        return 2;
    }

    private void commandNotFound(CommandEnvironment context) {
        context.getChannel().sendMessage(createEmbed(Colours.ERROR).setTitle("Invalid command").setDescription("No custom commands were found by your input.").build()).queue();
    }
}