package TypingGame;

import java.util.ArrayList;
import java.util.List;

public class GameLevel {

    private final List<LevelObserver> observers;  // List of observers (Enemies, Background)
    private int currentLevel;  // The current game level

    public GameLevel() {
        this.currentLevel = 1;  // Start at level 1
        this.observers = new ArrayList<>();
    }

    // Method to add observers (Enemies, Background Image controllers)
    public void addObserver(LevelObserver observer) {
        observers.add(observer);
    }

    // Method to remove observers
    public void removeObserver(LevelObserver observer) {
        observers.remove(observer);
    }

    // Method to notify all observers when the level changes
    public void notifyObservers() {
        for (LevelObserver observer : observers) {
            observer.updateLevel(currentLevel);
        }
    }

    // Method to increase the level
    public void nextLevel() {
        currentLevel++;  // Increment level
        notifyObservers();  // Notify all observers about the level change
    }

    public int getCurrentLevel() {
        return currentLevel;
    }
}
