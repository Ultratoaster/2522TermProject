package NumberGame;

/**
 * Interface representing the essential events for a number placement game.
 * It provides methods for starting the game, placing numbers, checking the grid's order, and showing the result.
 * Implementing classes must define the behavior of each of these methods.
 *
 * @author Ben Henry
 * @version 1.0
 */
public interface GameEvent {

    /**
     * Starts a new game by initializing all necessary components.
     * Implementations should define the specific steps to begin a new game.
     */
    void startGame();

    /**
     * Places the given number at the specified position on the grid.
     *
     * @param number   The number to be placed on the grid.
     * @param position The position on the grid where the number is placed.
     */
    void placeNumber(int number, int position);

    /**
     * Checks if the grid is in ascending order (from left to right).
     * This method can be used to determine if the numbers on the grid are placed in the correct order.
     *
     * @return True if the grid is in order, false otherwise.
     */
    boolean isGridInOrder();

    /**
     * Displays the result of the game, indicating whether the player has won or lost.
     *
     * @param won True if the player won the game, false if the player lost.
     */
    void showGameResult(boolean won);
}
