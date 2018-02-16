package me.blayyke.reflex.command.commands.developer;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandContext;
import me.blayyke.reflex.game.MineSweeperGame;
import me.blayyke.reflex.game.MineSweeperManager;
import me.blayyke.reflex.utils.MiscUtils;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class CommandMinesweeper extends AbstractCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.DEVELOPER;
    }

    @Override
    public String getName() {
        return "minesweeper";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"ms"};
    }

    @Override
    public String getDesc() {
        return "Play a game of minesweeper";
    }

    @Override
    public void execute(CommandContext context) {
        MineSweeperManager manager = getReflex().getDataManager().getGuildStorage(context.getGuild()).getMineSweeperManager();

        if (!manager.userHasGame(context.getMember().getUser())) {
            if (context.getArgs().length < 2) {
                context.getChannel().sendMessage("Missing required args: <board size> <mine chance>").queue();
                return;
            }
            if (!MiscUtils.isId(context.getArgs()[0])) {
                context.getChannel().sendMessage("Argument <1 / board size> must be a number!").queue();
                return;
            }
            if (!MiscUtils.isDouble(context.getArgs()[1])) {
                context.getChannel().sendMessage("Argument <2 / mine chance> must be a decimal number!").queue();
                return;
            }
            int boardSize = Integer.parseInt(context.getArgs()[0]);
            double mineChance = Double.parseDouble(context.getArgs()[1]);

            if (boardSize > MineSweeperManager.MAX_BOARD_SIZE || boardSize < MineSweeperManager.MIN_BOARD_SIZE) {
                context.getChannel().sendMessage("Board size must be between " + MineSweeperManager.MIN_BOARD_SIZE + " and " + MineSweeperManager.MAX_BOARD_SIZE + ".").queue();
                return;
            }
            if (mineChance > MineSweeperManager.MAX_MINE_CHANCE || mineChance < MineSweeperManager.MIN_MINE_CHANCE) {
                context.getChannel().sendMessage("Mine chance size must be between " + MineSweeperManager.MIN_MINE_CHANCE + " and " + MineSweeperManager.MAX_MINE_CHANCE + ".").queue();
                return;
            }

            Message message = context.getChannel().sendMessage("Creating game...").complete();
            MineSweeperGame game = manager.createGame(context.getMessage().getAuthor(), message);
            game.startGame(boardSize, mineChance);
            message.editMessage("Game created:\n\n" + game.getBlankBoard()).queue();
            return;
        }
        MineSweeperGame game = getReflex().getDataManager().getGuildStorage(context.getGuild()).getMineSweeperManager().getGame(context.getMember().getUser());

        if (context.getArgs().length < 2) {
            context.getChannel().sendMessage("Missing required args: <x> <y> [flag]").queue();
            return;
        }

        if (!MiscUtils.isId(context.getArgs()[0])) {
            context.getChannel().sendMessage("Argument <1 / x-coordinate> must be a number!").queue();
            return;
        }
        if (!MiscUtils.isId(context.getArgs()[1])) {
            context.getChannel().sendMessage("Argument <2 / y-coordinate> must be a number!").queue();
            return;
        }

        int inputX = Integer.parseInt(context.getArgs()[0]);
        int inputY = Integer.parseInt(context.getArgs()[1]);
        if (context.getArgs().length > 2) {
            if (!context.getArgs()[2].equalsIgnoreCase("flag")) {
                context.getChannel().sendMessage("Argument <3 / string> must equal `flag`!").queue();
                return;
            }
            game.flagInput(context.getChannel(), inputX, inputY);
        } else
            game.input(context.getChannel(), inputX, inputY);

        if (context.getGuild().getSelfMember().hasPermission(context.getChannel(), Permission.MESSAGE_MANAGE))
            context.getMessage().delete().queue();
    }
}