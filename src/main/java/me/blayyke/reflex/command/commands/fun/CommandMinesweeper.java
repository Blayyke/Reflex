package me.blayyke.reflex.command.commands.fun;

import me.blayyke.reflex.Colours;
import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandEnvironment;
import me.blayyke.reflex.game.MineSweeperGame;
import me.blayyke.reflex.game.MineSweeperManager;
import me.blayyke.reflex.utils.MiscUtils;
import net.dv8tion.jda.core.EmbedBuilder;
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
    public void onCommand(CommandEnvironment env) {
        MineSweeperManager manager = getReflex().getDataManager().getGuildStorage(env.getGuild()).getMineSweeperManager();
        EmbedBuilder embedBuilder = createEmbed();
        embedBuilder.setTitle("Minesweeper");

        if (!manager.userHasGame(env.getMember().getUser())) {
            if (env.getArgs().length < 2) {
                embedBuilder.setColor(Colours.WARN);

                embedBuilder.setDescription("Missing required args: <board size> <mine chance>");
                env.getChannel().sendMessage(embedBuilder.build()).queue();
                return;
            }
            if (!MiscUtils.isId(env.getArgs()[0])) {
                embedBuilder.setColor(Colours.WARN);

                embedBuilder.setDescription("Argument <1 / board size> must be a number!");
                env.getChannel().sendMessage(embedBuilder.build()).queue();
                return;
            }
            if (!MiscUtils.isDouble(env.getArgs()[1])) {
                embedBuilder.setColor(Colours.WARN);

                embedBuilder.setDescription("Argument <2 / mine chance> must be a decimal number!");
                env.getChannel().sendMessage(embedBuilder.build()).queue();
                return;
            }
            int boardSize = Integer.parseInt(env.getArgs()[0]);
            double mineChance = Double.parseDouble(env.getArgs()[1]);

            if (boardSize > MineSweeperManager.MAX_BOARD_SIZE || boardSize < MineSweeperManager.MIN_BOARD_SIZE) {
                embedBuilder.setColor(Colours.WARN);

                embedBuilder.setDescription("Board size must be between " + MineSweeperManager.MIN_BOARD_SIZE + " and " + MineSweeperManager.MAX_BOARD_SIZE + ".");
                env.getChannel().sendMessage(embedBuilder.build()).queue();
                return;
            }
            if (mineChance > MineSweeperManager.MAX_MINE_CHANCE || mineChance < MineSweeperManager.MIN_MINE_CHANCE) {
                embedBuilder.setColor(Colours.WARN);

                embedBuilder.setDescription("Mine chance size must be between " + MineSweeperManager.MIN_MINE_CHANCE + " and " + MineSweeperManager.MAX_MINE_CHANCE + ".");
                env.getChannel().sendMessage(embedBuilder.build()).queue();
                return;
            }

            Message message = env.getChannel().sendMessage("Creating game...").complete();
            MineSweeperGame game = manager.createGame(env.getMessage().getAuthor(), message);
            game.startGame(boardSize, mineChance);
            message.editMessage("Game created:\n\n" + game.getBlankBoard()).queue();
            return;
        }
        MineSweeperGame game = getReflex().getDataManager().getGuildStorage(env.getGuild()).getMineSweeperManager().getGame(env.getMember().getUser());

        if (env.getArgs().length < 2) {
            embedBuilder.setColor(Colours.WARN);

            embedBuilder.setDescription("Missing required args: <x> <y> [flag]");
            env.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        }

        if (!MiscUtils.isId(env.getArgs()[0])) {
            embedBuilder.setColor(Colours.WARN);

            embedBuilder.setDescription("Argument <1 / x-coordinate> must be a number!");
            env.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        }
        if (!MiscUtils.isId(env.getArgs()[1])) {
            embedBuilder.setColor(Colours.WARN);

            embedBuilder.setDescription("Argument <2 / y-coordinate> must be a number!");
            env.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        }

        int inputX = Integer.parseInt(env.getArgs()[0]);
        int inputY = Integer.parseInt(env.getArgs()[1]);

        if (env.getArgs().length > 2) {
            if (!env.getArgs()[2].equalsIgnoreCase("flag")) {
                embedBuilder.setColor(Colours.WARN);

                embedBuilder.setDescription("Argument <3 / string> must equal `flag`!");
                env.getChannel().sendMessage(embedBuilder.build()).queue();
                return;
            }
            game.flagInput(env.getChannel(), inputX, inputY);
        } else
            game.input(env.getChannel(), inputX, inputY);

        if (env.getGuild().getSelfMember().hasPermission(env.getChannel(), Permission.MESSAGE_MANAGE))
            env.getMessage().delete().queue();
    }

    @Override
    public int getRequiredArgs() {
        return 2;
    }
}