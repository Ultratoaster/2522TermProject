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
abstract class GameEventHandler
{


    private static final int RANDOM_NUMBER_UPPER_BOUND = 1000;
    private static final int RANDOM_PLUS_ONE_SHIFT = 1;
    public static final int INITIAL_SUCCESSFUL_PLACEMENTS = 0;

    final int[] grid;
    final List<Integer> validPositions;
    final Random random;
    final NumberGameStatsHandler stats;

    int currentNumber;
    int successfulPlacements;

    {
        grid = new int[NumberGame.BUTTON_COUNT];
        validPositions = new ArrayList<>();
        random = new Random();
        stats = new NumberGameStatsHandler();

        successfulPlacements = INITIAL_SUCCESSFUL_PLACEMENTS;
    }


    /**
     * Starts the game by initializing all required variables and generating the first random number.
     * Concrete implementations of this method should define the specific behavior of starting the game.
     */
    abstract void startGame();

    /**
     * Places the current number on the grid at the specified position.
     * Increments the successful placements counter.
     *
     * @param position The position on the grid where the number is placed.
     */
    abstract void placeNumberOnGrid(final int position);

    /**
     * Generates the next random number to be placed on the grid.
     * Concrete implementations of this method should define the specific behavior of generating the next number.
     */
    abstract void generateNextNumber();

    /**
     * Checks for available placements for the current number on the grid.
     * Concrete implementations of this method should define how to find valid placements.
     */
    abstract void checkAvailablePlacements();

    /**
     * Checks if the game is over, typically when there are no valid placements left.
     *
     * @return True if the game is over, false otherwise.
     */
    abstract boolean checkGameOver();

    /**
     * Resets the game state, including clearing the grid and resetting placement counters.
     * Concrete implementations should define how to reset the game state.
     */
    abstract void resetGame();

    /**
     * Generates a random number between 1 and the upper bound (inclusive).
     * The number generated is assigned to the currentNumber field.
     */
    void generateRandomNumber()
    {
        currentNumber = random.nextInt(RANDOM_NUMBER_UPPER_BOUND) + RANDOM_PLUS_ONE_SHIFT;
    }

    /**
     * Returns the current statistics as a formatted string.
     * This includes the number of wins, losses, and successful placements.
     *
     * @return A string containing the game statistics.
     */
    String getStatistics()
    {
        return stats.getStatistics();
    }

    /**
     * Records a win in the game statistics.
     * This updates the win count and adds the number of successful placements to the stats.
     */
    void recordWin()
    {
        stats.recordWin(successfulPlacements);
    }

    /**
     * Records a loss in the game statistics.
     * This updates the loss count and adds the number of successful placements to the stats.
     */
    void recordLoss()
    {
        stats.recordLoss(successfulPlacements);
    }

}
