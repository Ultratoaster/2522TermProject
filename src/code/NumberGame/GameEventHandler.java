package NumberGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Abstract class to handle the game logic for the Number Game.
 * It manages the grid, current number, successful placements, and valid positions on the grid.
 * This class also interfaces with the stats handler to track wins, losses, and placements.
 *
 * @author Ben Henry
 * @version 1.0
 */
public abstract class GameEventHandler {

    public static final int RANDOM_NUMBER_UPPER_BOUND = 1000;
    public static final int RANDOM_PLUS_ONE_SHIFT = 1;

    protected int[] grid = new int[NumberGame.BUTTON_COUNT];  // Array to hold the numbers placed on the grid
    protected int currentNumber;  // Current number to place on the grid
    protected int successfulPlacements = 0;  // Number of successful placements made in the current game
    protected List<Integer> validPositions = new ArrayList<>();  // List of valid positions on the grid for placing the current number
    protected Random random = new Random();  // Random number generator instance for generating random numbers
    protected NumberGameStatsHandler stats = new NumberGameStatsHandler();  // Stats handler instance

    /**
     * Starts the game by initializing all required variables and generating the first random number.
     * Concrete implementations of this method should define the specific behavior of starting the game.
     */
    public abstract void startGame();

    /**
     * Places the current number on the grid at the specified position.
     * Increments the successful placements counter.
     *
     * @param position The position on the grid where the number is placed.
     */
    public abstract void placeNumberOnGrid(final int position);

    /**
     * Generates the next random number to be placed on the grid.
     * Concrete implementations of this method should define the specific behavior of generating the next number.
     */
    public abstract void generateNextNumber();

    /**
     * Checks for available placements for the current number on the grid.
     * Concrete implementations of this method should define how to find valid placements.
     */
    public abstract void checkAvailablePlacements();

    /**
     * Checks if the game is over, typically when there are no valid placements left.
     *
     * @return True if the game is over, false otherwise.
     */
    public abstract boolean checkGameOver();

    /**
     * Displays the result of the game, indicating whether the game was won or lost.
     *
     * @param won True if the game was won, false if it was lost.
     */
    public abstract void showGameResult(final boolean won);

    /**
     * Resets the game state, including clearing the grid and resetting placement counters.
     * Concrete implementations should define how to reset the game state.
     */
    public abstract void resetGame();

    /**
     * Generates a random number between 1 and the upper bound (inclusive).
     * The number generated is assigned to the currentNumber field.
     */
    protected void generateRandomNumber() {
        currentNumber = random.nextInt(RANDOM_NUMBER_UPPER_BOUND) + RANDOM_PLUS_ONE_SHIFT;
    }

    /**
     * Returns the current statistics as a formatted string.
     * This includes the number of wins, losses, and successful placements.
     *
     * @return A string containing the game statistics.
     */
    public String getStatistics() {
        return stats.getStatistics();
    }

    /**
     * Records a win in the game statistics.
     * This updates the win count and adds the number of successful placements to the stats.
     */
    public void recordWin() {
        stats.recordWin(successfulPlacements);
    }

    /**
     * Records a loss in the game statistics.
     * This updates the loss count and adds the number of successful placements to the stats.
     */
    public void recordLoss() {
        stats.recordLoss(successfulPlacements);
    }

    /**
     * Resets the game statistics.
     * This clears all win, loss, and placement records.
     */
    public void resetStats() {
        stats.resetStats();
    }
}
