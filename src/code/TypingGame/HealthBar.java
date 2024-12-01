package TypingGame;

import javafx.geometry.Insets;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

public class HealthBar {
    private ProgressBar progressBar;
    private VBox container;
    private String style;
    private int height;
    private int width;
    private int margin;

    // Constructor to initialize the health bar
    public HealthBar(double initialValue, String style, int height, int width, int margin) {
        this.style = style;
        this.height = height;
        this.width = width;
        this.margin = margin;

        // Create and setup the ProgressBar
        this.progressBar = new ProgressBar(initialValue);
        this.progressBar.setPrefHeight(height);
        this.progressBar.setPrefWidth(width);
        this.progressBar.setStyle(style);
    }

    // Initialize the container with the health bar
    public VBox setupHealthBar(VBox container) {
        this.container = container;
        VBox.setMargin(progressBar, new Insets(margin));
        container.getChildren().add(progressBar);
        return container;
    }

    // Method to update the health bar's progress
    public void updateHealth(double healthPercentage) {
        progressBar.setProgress(healthPercentage);
    }

    // Method to get the ProgressBar (useful if you need to access it directly)
    public ProgressBar getProgressBar() {
        return progressBar;
    }
}
