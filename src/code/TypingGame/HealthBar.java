package TypingGame;

import javafx.geometry.Insets;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

/**
 * Represents a customizable health bar that visually displays health as a progress bar.
 * It can be styled and updated dynamically during the game.
 *
 * @author Ben Henry
 * @version 1.0
 */
class HealthBar
{
    private final ProgressBar progressBar;
    private final int margin;

    /**
     * Constructs a new HealthBar with specified properties.
     *
     * @param initialValue The initial health value as a percentage (0.0 to 1.0).
     * @param style        The CSS style string for the health bar.
     * @param height       The height of the health bar in pixels.
     * @param width        The width of the health bar in pixels.
     * @param margin       The margin around the health bar.
     */
    HealthBar(final double initialValue, final String style, final int height, final int width, final int margin)
    {
        this.margin = margin;
        this.progressBar = new ProgressBar(initialValue);
        this.progressBar.setPrefHeight(height);
        this.progressBar.setPrefWidth(width);
        this.progressBar.setStyle(style);
    }

    /**
     * Adds the health bar to the specified VBox container with the configured margin.
     *
     * @param container The VBox container to which the health bar will be added.
     */
    public void setupHealthBar(final VBox container)
    {
        VBox.setMargin(progressBar, new Insets(margin));
        container.getChildren().add(progressBar);
    }

    /**
     * Updates the health bar's progress based on the given health percentage.
     *
     * @param healthPercentage The new health value as a percentage (0.0 to 1.0).
     */
    public void updateHealth(final double healthPercentage)
    {
        progressBar.setProgress(healthPercentage);
    }

    /**
     * Retrieves the ProgressBar instance of this health bar.
     *
     * @return The ProgressBar associated with this health bar.
     */
    public ProgressBar getProgressBar()
    {
        return progressBar;
    }
}
