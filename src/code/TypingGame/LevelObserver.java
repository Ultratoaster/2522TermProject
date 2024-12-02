package TypingGame;

/**
 * Interface for objects that observe and respond to level changes in the game.
 *
 * @author Ben Henry
 * @version 1.0
 */
public interface LevelObserver
{

    /**
     * Updates the observer with the new level.
     *
     * @param level The current game level.
     */
    void updateLevel(final int level);
}