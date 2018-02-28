package me.blayyke.reflex.command.commands.customization;

import me.blayyke.reflex.Colours;
import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandContext;
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
    public void onCommand(CommandContext context) {
        EmbedBuilder embed = createEmbed();

        switch (context.getArgs()[0].toLowerCase()) {
            case "create":
                if (getReflex().getCustomCommandManager().commandExists(context.getGuild(), context.getArgs()[1])) {
                    replyError(context, "A command already exists with that name. If you would like to delete this command, use `" + context.getPrefixUsed() + context.getAlias() + " delete <name>.");
                    return;
                }
                if (getReflex().getCommandManager().getCommand(context.getArgs()[1]) != null) {
                    replyError(context, "A default command already exists with that name. Please use another name.");
                    return;
                }

                CustomCommand command = new CustomCommand(getReflex(), context.getGuild(), context.getArgs()[1]);
                command.setAction(null);
                command.setCreatorId(context.getMember().getUser().getIdLong());
                getReflex().getCustomCommandManager().createCommand(command);
                context.getChannel().sendMessage(embed.setTitle("Created command").setDescription("Successfully created command `" + command.getName() + "`.").build()).queue();
                break;
            case "action": {
                String s = context.getArgs()[1];
                CustomCommand c = getReflex().getCustomCommandManager().getCommand(context.getGuild(), s);
                if (c == null) {
                    commandNotFound(context);
                    return;
                }

                c.setAction(MiscUtils.arrayToString(context.getArgs(), 2, " "));
                embed.setTitle("Command updated").setDescription("The action for that command has been updated.");
                context.getChannel().sendMessage(embed.build()).queue();
                break;
            }
            case "description":
            case "desc": {
                String s = context.getArgs()[1];
                CustomCommand c = getReflex().getCustomCommandManager().getCommand(context.getGuild(), s);
                if (c == null) {
                    commandNotFound(context);
                    return;
                }

                c.setDesc(MiscUtils.arrayToString(context.getArgs(), 2, " "));
                context.getChannel().sendMessage(embed.setTitle("Command updated").setDescription("Changed description of " + c.getName() + " to `" + c.getDesc() + "`.").build()).queue();
                break;
            }
            case "delete": {
                String s = context.getArgs()[1];
                CustomCommand c = getReflex().getCustomCommandManager().getCommand(context.getGuild(), s);
                if (c == null) {
                    commandNotFound(context);
                    return;
                }

                getReflex().getCustomCommandManager().deleteCommand(c);
                embed.setTitle("Command deleted");
                embed.setDescription("Command `" + c.getName() + "` has successfully been deleted.");
                context.getChannel().sendMessage(embed.build()).queue();
                break;
            }
            case "mode":
            case "type": {
                String s = context.getArgs()[1];
                CustomCommand c = getReflex().getCustomCommandManager().getCommand(context.getGuild(), s);
                if (c == null) {
                    commandNotFound(context);
                    return;
                }

                CustomCommandType customCommandType = Arrays.stream(CustomCommandType.values()).filter(type -> type.toString().equalsIgnoreCase(context.getArgs()[2])).findAny().orElse(null);

                if (customCommandType == null) {
                    embed.setTitle("Invalid type");
                    replyError(context, "`" + TextUtils.escapeFormatting(context.getArgs()[2]) + "` is not a valid command type. Valid types are: " + MiscUtils.arrayToString(CustomCommandType.values(), ", "));

                    context.getChannel().sendMessage(embed.build()).queue();
                    return;
                }

                c.setType(customCommandType);
                embed.setTitle("Command type updated");
                embed.setDescription("Changed command type of " + c.getName() + " to " + c.getType().toString() + ".");

                context.getChannel().sendMessage(embed.build()).queue();
                break;
            }
            default:
                embed.setTitle("Unknown argument(s)");
                embed.setDescription("Please provide a valid method argument. \nValid arguments include: ");

                context.getChannel().sendMessage(embed.build()).queue();
                break;
        }

    }

    @Override
    public int getRequiredArgs() {
        return 2;
    }

    private void commandNotFound(CommandContext context) {
        context.getChannel().sendMessage(createEmbed(Colours.ERROR).setTitle("Invalid command").setDescription("No custom commands were found by your input.").build()).queue();
    }
}