package TypingGame;

import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class GameTimer {

    private PauseTransition timer;
    private final double timePerLetter;
    private final Runnable onTimerExpired;

    /**
     * Creates a GameTimer instance.
     *
     * @param timePerLetter   Time allowed per letter in seconds.
     * @param onTimerExpired  Action to perform when the timer expires.
     */
    public GameTimer(double timePerLetter, Runnable onTimerExpired) {
        this.timePerLetter = timePerLetter;
        this.onTimerExpired = onTimerExpired;
    }

    /**
     * Starts the timer for a given word length.
     *
     * @param wordLength The length of the word to calculate total time.
     */
    public void startTimer(int wordLength) {
        stopTimer(); // Ensure no previous timer is running
        double duration = wordLength * timePerLetter;
        timer = new PauseTransition(Duration.seconds(duration));
        timer.setOnFinished(event -> onTimerExpired.run());
        timer.play();
    }

    /**
     * Stops the current timer, if running.
     */
    public void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }
}
