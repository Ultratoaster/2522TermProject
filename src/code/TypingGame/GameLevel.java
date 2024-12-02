package TypingGame;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the game's current level and notifies registered observers when the level changes.
 */
class GameLevel
{

    private final List<LevelObserver> observers;
    private int currentLevel;

    /**
     * Constructs a GameLevel instance starting at level 1.
     */
    GameLevel()
    {
        this.currentLevel = 1;
        this.observers = new ArrayList<>();
    }

    /**
     * Registers a new observer to be notified of level changes.
     *
     * @param observer The observer to add.
     */
    void addObserver(LevelObserver observer)
    {
        observers.add(observer);
    }

    /**
     * Unregisters an observer, removing it from the notification list.
     *
     * @param observer The observer to remove.
     */
    void removeObserver(LevelObserver observer)
    {
        observers.remove(observer);
    }

    /**
     * Notifies all registered observers of the current level.
     */
    void notifyObservers()
    {
        for(LevelObserver observer : observers)
        {
            observer.updateLevel(currentLevel);
        }
    }

    /**
     * Advances to the next level and notifies observers of the change.
     */
    void nextLevel()
    {
        currentLevel++;
        notifyObservers();
    }

    /**
     * Retrieves the current level.
     *
     * @return The current game level.
     */
    int getCurrentLevel()
    {
        return currentLevel;
    }
}
