package NumberGame;

import java.util.Arrays;

/**
 * Handles the game logic for the Number Placement game.
 * This class manages the grid, places numbers, tracks successful placements,
 * and determines valid positions for the next number to be placed.
 * It also checks for game-over conditions and displays game results.
 *
 * @author Ben Henry
 * @version 1.0
 */
class NumberPlacementGameHandler extends GameEventHandler
{


    private static final int INITIAL_SUCCESSFUL_PLACEMENTS = 0;
    private static final int FIRST_GRID_INDEX = 0;
    private static final int ADD_FOR_NEXT_INDEX = 1;
    private static final int INITIAL_LAST_SMALLER_INDEX = -1;

    static final int DEFAULT_GRID_FILL = 0;

    /**
     * Starts a new game by resetting values and preparing the grid.
     */
    @Override
    void startGame()
    {
        successfulPlacements = INITIAL_SUCCESSFUL_PLACEMENTS;
        Arrays.fill(grid, DEFAULT_GRID_FILL);
        generateRandomNumber();
        checkAvailablePlacements();
    }

    /**
     * Places the current number on the grid at the given position and updates successful placements.
     *
     * @param position the position where the current number should be placed
     */
    @Override
    void placeNumberOnGrid(int position)
    {
        grid[position] = currentNumber;
        successfulPlacements++;
        checkAvailablePlacements();
    }

    /**
     * Generates the next random number and updates valid placements.
     */
    @Override
    void generateNextNumber()
    {
        generateRandomNumber();
        checkAvailablePlacements();
    }

    /**
     * Checks all valid placements for the current number on the grid.
     * Valid placements are spots between the last smaller number and the first greater number.
     */
    @Override
    void checkAvailablePlacements()
    {
        validPositions.clear();

        int lastSmallerIndex = INITIAL_LAST_SMALLER_INDEX;
        int firstGreaterIndex = grid.length;

        for(int i = 0; i < grid.length; i++)
        {
            if(grid[i] != FIRST_GRID_INDEX)
            {
                if(grid[i] < currentNumber)
                {
                    lastSmallerIndex = i;
                }
                else if(grid[i] > currentNumber && firstGreaterIndex == grid.length)
                {
                    firstGreaterIndex = i;
                }
            }
        }

        for(int i = lastSmallerIndex + ADD_FOR_NEXT_INDEX; i < firstGreaterIndex; i++)
        {
            if(grid[i] == FIRST_GRID_INDEX)
            {
                validPositions.add(i);
            }
        }
    }

    /**
     * Checks if the game is over. The game is over if there are no valid positions left.
     *
     * @return true if the game is over, otherwise false
     */
    @Override
    boolean checkGameOver()
    {
        return validPositions.isEmpty();
    }

    /**
     * Resets the game by clearing the grid and preparing for a new game.
     */
    @Override
    void resetGame()
    {
        Arrays.fill(grid, DEFAULT_GRID_FILL);
        successfulPlacements = INITIAL_SUCCESSFUL_PLACEMENTS;
        validPositions.clear();
        generateNextNumber();
        checkAvailablePlacements();
    }
}
