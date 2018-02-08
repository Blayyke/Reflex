package me.blayyke.reflex.command.commands.customization;

import me.blayyke.reflex.Colours;
import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandContext;
import me.blayyke.reflex.command.custom.CustomCommand;
import me.blayyke.reflex.utils.MiscUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;

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
    public void execute(CommandContext context) {
        if (!context.hasArgs()) {
            notEnoughArgs(context);
            return;
        }

        EmbedBuilder embed = createEmbed();

        switch (context.getArgs()[0].toLowerCase()) {
            case "create":
                if (getReflex().getCustomCommandManager().commandExists(context.getGuild(), context.getArgs()[1])) {
                    embed.setTitle("Command already exists");
                    embed.setDescription("I could not create that command as one already exists with that name. \n" +
                            "If you want to delete the other command, run `" + getReflex().getPrefix(context.getGuild()) + context.getAlias() + " delete <name>`.");

                    context.getChannel().sendMessage(embed.build()).queue();
                    return;
                }
                if (getReflex().getCommandManager().getCommand(context.getArgs()[1]) != null) {
                    embed.setTitle("Command already exists");
                    embed.setDescription("I could not create that command as an inbuilt command already exists with that name. Please rerun this command with a different name.");
                    context.getChannel().sendMessage(embed.build()).queue();
                    return;
                }

                CustomCommand command = new CustomCommand(getReflex(), context.getGuild(), context.getArgs()[1]);
                command.setAction(null);
                command.setCreatorId(context.getMember().getUser().getIdLong());
                getReflex().getCustomCommandManager().createCommand(command);
                context.getChannel().sendMessage(embed.setTitle("Created command").setDescription("Successfully created command `" + command.getName() + "`.").build()).queue();
                break;
            case "action": {
                if (context.getArgs().length < 2) {
                    notEnoughArgs(context);
                    return;
                }
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
                if (context.getArgs().length < 2) {
                    notEnoughArgs(context);
                    return;
                }
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
                if (context.getArgs().length < 2) {
                    notEnoughArgs(context);
                    return;
                }
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
            default:
                embed.setTitle("Unknown argument(s)");
                embed.setDescription("Please provide a valid method argument. \nValid arguments include: ");

                context.getChannel().sendMessage(embed.build()).queue();
                break;
        }

    }

    private void commandNotFound(CommandContext context) {
        context.getChannel().sendMessage(createEmbed(Colours.ERROR).setTitle("Invalid command").setDescription("No custom commands were found by your input.").build()).queue();
    }
}