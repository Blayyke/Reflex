package me.blayyke.reflex.game;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;

import java.util.HashMap;
import java.util.Map;

public class MineSweeperManager {
    private final Guild guild;

    public static String BOMB = "\uD83D\uDCA3";
    public static String DOT = "\uD83D\uDD35";
    public static String FLAG = "\uD83D\uDEA9";

    // time in minutes before the game is deleted due to no activity.
    public static int MAX_IDLE_TIME = 10;

    public static int MIN_BOARD_SIZE = 3;
    public static int MAX_BOARD_SIZE = 15;
    public static double MIN_MINE_CHANCE = 0.1;
    public static double MAX_MINE_CHANCE = 0.9;

    private Map<Long, MineSweeperGame> gameMap = new HashMap<>();

    public MineSweeperManager(Guild guild) {
        this.guild = guild;
    }

    public static String getNumberEmoji(int number) {
        if (number > 9 || number < 0) throw new IllegalArgumentException("number cannot be above 9 or lower than 0!");

        return number + "\u20E3";
    }

    public boolean userHasGame(User user) {
        // remove finished game.
        if (gameMap.containsKey(user.getIdLong()) && !gameMap.get(user.getIdLong()).isActive())
            gameMap.remove(user.getIdLong());

        return gameMap.containsKey(user.getIdLong());
    }

    public MineSweeperGame createGame(User user) {
        MineSweeperGame game = new MineSweeperGame(this, user);
        gameMap.put(user.getIdLong(), game);
        return game;
    }

    public MineSweeperGame getGame(User user) {
        return gameMap.get(user.getIdLong());
    }

    public void deleteGame(MineSweeperGame game) {
        gameMap.remove(game.getUser().getIdLong());
    }
}