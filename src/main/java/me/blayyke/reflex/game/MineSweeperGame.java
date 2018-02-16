package me.blayyke.reflex.game;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class MineSweeperGame {
    private final MineSweeperManager manager;
    private final User user;
    private final Message message;

    //Board width & height
    private int boardSize;

    //A percentage in decimal: eg 5% = 0.05
    double bombChance;

    //Whether or not this game is active
    boolean active = false;

    private boolean[][] uncoveredTiles;
    private boolean[][] bombs;
    private boolean[][] flags;

    public MineSweeperGame(MineSweeperManager manager, Message message, User user) {
        this.manager = manager;
        this.user = user;
        this.message = message;
    }

    public void startGame(int boardSize, double bombChance) {
        if (this.active) throw new IllegalStateException("tried to start an already-running game!");

        this.boardSize = boardSize;
        this.bombChance = bombChance;

        bombs = new boolean[this.boardSize + 2][boardSize + 2];
        uncoveredTiles = new boolean[this.boardSize + 2][boardSize + 2];
        flags = new boolean[this.boardSize + 2][boardSize + 2];

        createBombs();

        this.active = true;
    }

    public void input(TextChannel channel, int x, int y) {
        if (isTileUncovered(x, y)) {
            message.editMessage("The tile at " + x + "," + y + " has already been uncovered.\n\n" + getVisibleBoard()).queue();
            return;
        }
        if (isTileBomb(x, y)) {
            message.editMessage("You hit a bomb, game over!\n\n" + getFullBoard()).queue();
            endGame();
            return;
        }

        uncoverTile(x, y);
        if (isBoardComplete()) {
            message.editMessage("You completed the board, congratulations!\n\n" + getVisibleBoard()).queue();
            endGame();
            return;
        }

        message.editMessage(getVisibleBoard()).queue();
    }

    public void flagInput(TextChannel channel, int x, int y) {
        if (isTileUncovered(x, y)) {
            message.editMessage("The tile at " + x + "," + y + " has already been uncovered.\n\n" + getVisibleBoard()).queue();
            return;
        }
        if (isTileFlagged(x, y))
            setTileFlagged(x, y, false);
        else setTileFlagged(x, y, true);

        message.editMessage(getVisibleBoard()).queue();
    }

    private void endGame() {
        active = false;
        manager.deleteGame(this);
    }

    private String getVisibleBoard() {
        StringBuilder builder = new StringBuilder();
        for (int x = 1; x < boardSize + 1; x++) {
            for (int y = 1; y < boardSize + 1; y++) {
                if (isTileFlagged(x, y))
                    builder.append(MineSweeperManager.FLAG).append(" ");
                else if (!isTileUncovered(x, y))
                    builder.append(MineSweeperManager.DOT).append(" ");
                else builder.append(MineSweeperManager.getNumberEmoji(getBombsAroundTile(x, y))).append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public String getFullBoard() {
        StringBuilder builder = new StringBuilder();
        for (int x = 1; x < boardSize + 1; x++) {
            for (int y = 1; y < boardSize + 1; y++) {
                if (isTileBomb(x, y))
                    builder.append(MineSweeperManager.BOMB).append(" ");
                else builder.append(MineSweeperManager.getNumberEmoji(getBombsAroundTile(x, y))).append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    /**
     * See if these coordinates are inside the boards boundaries.
     *
     * @param x The x-coordinate to check.
     * @param y The y-coordinate to check.
     * @return Whether or not these coordinates are in the boundaries of the board.
     */
    public boolean isInBounds(int x, int y) {
        return (x > boardSize || y > boardSize) || (x < 1 || y < 1);
    }

    /**
     * Lay out the bombs on the board.
     */
    private void createBombs() {
        for (int x = 1; x <= boardSize; x++)
            for (int y = 1; y <= boardSize; y++)
                bombs[x][y] = (Math.random() < bombChance);
    }

    /**
     * Get a blank board using the current board size.
     *
     * @return A String with a blank layout of the current board.
     */
    public String getBlankBoard() {
        StringBuilder builder = new StringBuilder();
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) builder.append(MineSweeperManager.DOT).append(" ");
            builder.append('\n');
        }
        return builder.toString();
    }

    /**
     * See if the tile at the provided coordinates has already been uncovered by the player.
     *
     * @param x The x-coordinate to check
     * @param y The y-coordinate to check
     * @return Whether or not this tile has previously been uncovered.
     */
    public boolean isTileUncovered(int x, int y) {
        return uncoveredTiles[x][y];
    }

    /**
     * See if the tile at the provided coordinates is a bomb.
     *
     * @param x The x-coordinate to check
     * @param y The y-coordinate to check
     * @return Whether or not this tile is a bomb.
     */
    public boolean isTileBomb(int x, int y) {
        return bombs[x][y];
    }

    private boolean isTileFlagged(int x, int y) {
        return flags[x][y];
    }

    /**
     * Uncover a tile at the provided coordinates.
     *
     * @param x The x-coordinate to check
     * @param y The y-coordinate to check
     * @throws IllegalStateException if the tile is already uncovered or the tile is a bomb.
     */
    public void uncoverTile(int x, int y) {
        if (isTileUncovered(x, y)) throw new IllegalStateException("tile @ " + x + "," + y + " is already uncovered!");
        if (isTileBomb(x, y)) throw new IllegalStateException("tile @ " + x + "," + y + " is a bomb!");

        uncoveredTiles[x][y] = true;
    }

    private void setTileFlagged(int x, int y, boolean flag) {
        if (isTileUncovered(x, y)) throw new IllegalStateException("tile @ " + x + "," + y + " is already uncovered!");
        flags[x][y] = flag;
    }

    /**
     * See if the entire board has been uncovered successfully by the player.
     *
     * @return Whether or not the board is completely uncovered.
     */
    public boolean isBoardComplete() {
        for (int x = 1; x < boardSize + 1; x++)
            for (int y = 1; y < boardSize + 1; y++)
                if (!isTileUncovered(x, y) && !isTileBomb(x, y)) return false;

        return true;
    }


    /**
     * Get the amount of bombs around the tile at the provided coordinates.
     *
     * @param x The x-coordinate to check
     * @param y The y-coordinate to check
     * @return The amount of bombs that are around the tile at these coordinates.
     */
    public int getBombsAroundTile(int x, int y) {
        int bombs = 0;
        for (int x1 = x - 1; x1 <= x + 1; x1++)
            for (int y1 = y - 1; y1 <= y + 1; y1++)
                if (this.bombs[x1][y1]) bombs++;

        return bombs;
    }

    public boolean isActive() {
        return active;
    }

    public User getUser() {
        return user;
    }
}