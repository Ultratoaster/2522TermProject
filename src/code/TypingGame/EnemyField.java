package TypingGame;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.ProgressBar;

public class EnemyField {
    private VBox enemyBox;
    private ImageView enemyImageView;
    private ProgressBar enemyHealthBar;
    private Text enemyInfoText;

    // Constructor to initialize the enemy field
    public EnemyField() {
        // Initialize the enemy box container
        enemyBox = new VBox(10);
        enemyBox.setStyle("-fx-alignment: center;");

        // Initialize the enemy health bar
        enemyHealthBar = new ProgressBar(1.0);  // Start with full health
        enemyHealthBar.setPrefWidth(500);
        enemyHealthBar.setPrefHeight(30);
        enemyHealthBar.setStyle("-fx-accent: red;");

        // Initialize the enemy name text
        enemyInfoText = new Text("TypingGame.Enemy Name");

        // Initialize the enemy image view
        enemyImageView = new ImageView();
        enemyImageView.setFitWidth(250);
        enemyImageView.setFitHeight(250);

        // Add elements to the enemy box
        enemyBox.getChildren().addAll(enemyHealthBar, enemyInfoText, enemyImageView);
    }

    // Method to set up the enemy details
    public void setupEnemy(Enemy enemy) {
        // Update the enemy image
        Image enemyImage = new Image("file:" + enemy.getImageLocation());
        enemyImageView.setImage(enemyImage);

        // Update the enemy's name
        enemyInfoText.setText(enemy.getName());

        // Update the enemy's health bar
        double healthPercentage = (double) enemy.getHealth() / enemy.getMaxHealth();
        enemyHealthBar.setProgress(healthPercentage);
    }

    // Method to update the enemy health
    public void updateEnemyHealth(Enemy enemy) {
        double healthPercentage = (double) enemy.getHealth() / enemy.getMaxHealth();
        enemyHealthBar.setProgress(healthPercentage);
    }

    // Method to handle the transition to the next enemy
    public void nextEnemy(Enemy nextEnemy) {
        setupEnemy(nextEnemy);
    }

    // Getter method for the enemy VBox container
    public VBox getEnemyBox() {
        return enemyBox;
    }

    // Getter methods for individual components, if needed
    public ProgressBar getEnemyHealthBar() {
        return enemyHealthBar;
    }

    public Text getEnemyInfoText() {
        return enemyInfoText;
    }

    public ImageView getEnemyImageView() {
        return enemyImageView;
    }
}
