package TypingGame;

public interface HealthObservable {
    void addObserver(HealthObserver observer);
    void removeObserver(HealthObserver observer);
    void notifyObservers();
}
