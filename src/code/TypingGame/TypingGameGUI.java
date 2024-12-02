package TypingGame;

import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class TypingGameGUI extends Application
{

    // Layout
    public static final int VBOX_SPACING = 10;
    public static final String ENEMY_VBOX_STYLE = "-fx-margin: 20px;";
    public static final double PLAYER_VBOX_HEIGHT_FACTOR = 0.2;
    public static final int GENERIC_FONT_SIZE = 24;
    public static final int DEFAULT_SCENE_HEIGHT_PX = 900;
    public static final int ENEMY_IMAGE_WIDTH = 250;
    public static final int ENEMY_IMAGE_HEIGHT = 250;
    public static final String INITIAL_ENEMY_INFO_TEXT = "";
    public static final int DEFAULT_SCENE_WIDTH_PX = 600;
    public static final int BOSS_LEVEL = 10;
    public static final String BOSS_NAME = "Dog.reverse()";
    public static final int BOSS_HP_SCALE_FACTOR = 50000;
    public static final String BOSS_IMAGE_LOCATION = "images/dog.reverse().png";
    private BackgroundImageController backgroundImageController;

    private PauseTransition wordTimer; // Timer for word input
    private static final double BASE_TIME_PER_LETTER = 1.2; // Base time in seconds per letter
    private static final double SCALING_FACTOR = 0.1; // Decreases time per letter as level increases
    private static final double MIN_TIME_PER_LETTER = 0.3;
    private TranslateTransition wordMovementAnimation;



    // Health Bars
    public static final int HEALTH_BAR_HEIGHT = 30;
    public static final int HEALTH_BAR_WIDTH = 500;
    public static final int HEALTH_BAR_MARGIN = 25;
    public static final double PROGRESS_BAR_MAX_PERCENT = 100.0;
    public static final double INITIAL_HEALTH_BAR_VALUE = 1.0;
    public static final String PLAYER_HEALTH_BAR_STYLE = "-fx-accent: green;";
    public static final String ENEMY_HEALTH_BAR_STYLE = "-fx-accent: red;";
    private HealthBar enemyHealthBar;
    private HealthBar playerHealthBar;

    // Text and input variables
    public static final int CHARACTER_INPUT_PREF_WIDTH = 45;
    public static final int CHARACTER_INPUT_MAX_WIDTH = 45;
    public static final String CHARACTER_INPUT_PROMPT_TEXT = "_";
    public static final String CHARACTER_INPUT_STYLE = "-fx-background-color: rgba(169, 169, 169, 0.5); -fx-border-width: 0 0 2px 0; -fx-border-color: black; -fx-font-size: 14px;";
    public static final String ENEMY_TEXT_STYLE = "-fx-effect: dropshadow(three-pass-box, white, 1, 1, 0, 0); -fx-background-color: rgba(169, 169, 169, 0.5); -fx-font-size: 30px;";
    public static final String WORD_TO_ENTER_TEXT_STYLE = "-fx-effect: dropshadow(three-pass-box, white, 1, 1, 0, 0); -fx-background-color: rgba(169, 169, 169, 0.5); -fx-font-size: 20px;";
    private TextField[] textFields;
    private Text enemyInfoText;
    private Text wordToTypeText;

    // Gameplay elements
    public static final int PLAYER_DAMAGE_AMOUNT = 500;
    private int playerHealth = 100;
    private List<String> javaTerms;
    private List<Enemy> enemies;
    private int currentEnemyIndex;
    private String currentWord;
    private GameLevel gameLevel;
    private Stage primaryStage;
    private boolean bossFight = false; // Tracks if the boss fight has started


    public static final int ZEROTH_INDEX = 0;

    VBox root;
    Image backgroundImage;
    BackgroundImage bgImage;

    VBox enemyBox;
    VBox gameBox;
    VBox playerBox;
    HBox textEntryBox;

    /**
     * Sets up the main game scene, loading fonts, background, and UI components.
     * @param primaryStage The primary stage for this application.
     */
    @Override
    public void start(Stage primaryStage)
    {

        Platform.setImplicitExit(false);
        this.primaryStage = primaryStage;

        root = new VBox(VBOX_SPACING);
        backgroundImageController = new BackgroundImageController();
        backgroundImage = new Image(backgroundImageController.getBackground());
        bgImage = new BackgroundImage(backgroundImage, null, null, null, BackgroundSize.DEFAULT);
        root.setBackground(new Background(bgImage));

        final String fontPath;
        final Font customFont;

        fontPath = Paths.get("src", "resources", "fonts", "joystix.otf").toUri().toString();
        customFont = Font.loadFont(fontPath, GENERIC_FONT_SIZE);
        root.setStyle("-fx-font-family: '" + customFont.getName() + "';");

        loadWords();

        currentWord = WordLoader.getRandomWord(javaTerms);

        gameLevel = new GameLevel();
        gameLevel.addObserver(backgroundImageController);

        loadEnemies();

        for(Enemy enemy : enemies)
        {
            gameLevel.addObserver(enemy);
        }

        currentEnemyIndex = ZEROTH_INDEX;

        setUpUI();

        createTextFields();

        playerBox.getChildren().add(textEntryBox);

        playerHealthBar = new HealthBar(100 / PROGRESS_BAR_MAX_PERCENT,
                PLAYER_HEALTH_BAR_STYLE, HEALTH_BAR_HEIGHT,
                HEALTH_BAR_WIDTH, HEALTH_BAR_MARGIN);
        playerHealthBar.setupHealthBar(playerBox);

        root.getChildren().addAll(enemyBox, gameBox);

        VBox.setVgrow(gameBox, javafx.scene.layout.Priority.ALWAYS);

        root.getChildren().add(playerBox);

        playerBox.setPrefHeight(PLAYER_VBOX_HEIGHT_FACTOR * DEFAULT_SCENE_HEIGHT_PX);

        Scene scene = new Scene(root, DEFAULT_SCENE_WIDTH_PX, DEFAULT_SCENE_HEIGHT_PX);
        primaryStage.setTitle("Typing Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> stopTimer());

        startGame();
    }

    /**
     * Starts the game by displaying the current enemy and initializing the gameplay.
     */
    private void startGame()
    {

        updateEnemyDisplay(false);
    }



    //Game Setup Methods

    /**
     * Loads the enemy data from a file.
     * The enemies are loaded into the game from a pre-defined data source.
     */
    private void loadEnemies()
    {
        try
        {
            enemies = EnemyLoader.loadEnemies();
        } catch(IOException e)
        {
            System.err.println("Failed to load enemies. Please check the enemies data file or path.");
            e.printStackTrace();
        }
    }


    /**
     * Loads the word list from a file.
     * The words are used in the game for the player to type.
     */
    private void loadWords()
    {
        try
        {
            javaTerms = WordLoader.loadWords("src/resources/terms.txt");  // Load words from the file
        } catch(IOException e)
        {
            System.err.println("Error loading words from file");
            e.printStackTrace();
        }
    }


    /**
     * Sets up the user interface components, including enemy box, health bars, and word display.
     */
    private void setUpUI()
    {
        enemyBox = new VBox(VBOX_SPACING);
        enemyBox.setAlignment(Pos.CENTER);
        enemyBox.setStyle(ENEMY_VBOX_STYLE);

        enemyHealthBar = new HealthBar(INITIAL_HEALTH_BAR_VALUE, ENEMY_HEALTH_BAR_STYLE,
                HEALTH_BAR_HEIGHT, HEALTH_BAR_WIDTH, HEALTH_BAR_MARGIN);
        enemyHealthBar.setupHealthBar(enemyBox);

        enemyInfoText = new Text(INITIAL_ENEMY_INFO_TEXT);
        enemyInfoText.setStyle(ENEMY_TEXT_STYLE);
        enemyBox.getChildren().add(enemyInfoText);


        gameBox = new VBox(VBOX_SPACING);
        gameBox.setAlignment(Pos.TOP_CENTER);
        wordToTypeText = new Text(currentWord);
        wordToTypeText.setStyle(WORD_TO_ENTER_TEXT_STYLE);
        gameBox.getChildren().add(wordToTypeText);

        playerBox = new VBox(VBOX_SPACING);
        textEntryBox = new HBox(VBOX_SPACING);
        playerBox.setAlignment(Pos.CENTER);
        textEntryBox.setAlignment(Pos.CENTER);

        textFields = new TextField[currentWord.length()];
    }


    //UI Methods
    /**
     * Creates and configures the text fields for the current word.
     * Each text field allows the player to type one character of the word.
     */
    private void createTextFields()
    {
        for(int i = ZEROTH_INDEX; i < currentWord.length(); i++)
        {
            TextField textField = getTextField(i);

            textFields[i] = textField;  // Store the text field in the array
            textField.setOnKeyPressed(event -> {
                if(event.getCode() == KeyCode.TAB)
                {
                    textField.requestFocus();
                }
            });
            textEntryBox.getChildren().add(textField);  // Add it to the layout
        }
    }


    /**
     * Returns a new text field configured for the given index.
     * @param i The index of the current word.
     * @return The newly created TextField.
     */
    private TextField getTextField(final int i)
    {
        final TextField textField;
        textField = new TextField();
        textField.setPrefWidth(CHARACTER_INPUT_PREF_WIDTH);
        textField.setMaxWidth(CHARACTER_INPUT_MAX_WIDTH);
        textField.setStyle(CHARACTER_INPUT_STYLE);
        textField.setPromptText(CHARACTER_INPUT_PROMPT_TEXT);
        textField.setAlignment(Pos.CENTER);
        textField.setMouseTransparent(true);


        textField.setOnKeyTyped(_ -> handleCharacterInput(i));
        return textField;
    }

    /**
     * Updates the background of the game by removing the existing background image and loading a new one.
     * This method first checks if there are any existing `ImageView` nodes in the root layout and removes them.
     * Then, it retrieves the new background image from the `BackgroundImageController` and sets it as the background of the root layout.
     */
    private void updateBackground()
    {
        if(!root.getChildren().isEmpty())
        {
            root.getChildren().removeIf(node -> node instanceof ImageView);
        }

        backgroundImage = new Image(backgroundImageController.getBackground());
        bgImage = new BackgroundImage(backgroundImage, null, null, null, BackgroundSize.DEFAULT);
        root.setBackground(new Background(bgImage));
    }



    //Game Methods
    /**
     * Moves to the next enemy or triggers a boss fight if the current level is the boss level.
     * If the player has defeated the current enemy, the method proceeds to the next enemy in the list.
     * If all enemies are defeated, the index is reset to 0. If the boss level is reached, a new boss enemy is created and added to the list.
     * Updates the game level and the enemy display after each transition.
     */
    private void nextEnemy() {
        if (bossFight && enemies.get(currentEnemyIndex).isDefeated())
        {
            gameOver(true); // Victory
            return;
        }

        currentEnemyIndex++; // Move to the next enemy
        gameLevel.nextLevel(); // Move to the next level

        if (gameLevel.getCurrentLevel() == BOSS_LEVEL) {
            Enemy boss = new Enemy(BOSS_NAME, PLAYER_DAMAGE_AMOUNT * BOSS_HP_SCALE_FACTOR, BOSS_IMAGE_LOCATION);
            bossFight=true;
            enemies.add(boss);  // Add boss as the last enemy
        }

        // Check if we have defeated all enemies in the current level
        if (currentEnemyIndex >= enemies.size()) {
            currentEnemyIndex = 0;
        }

        // Update the enemy display
        updateBackground();
        updateEnemyDisplay(bossFight);
    }

    /**
     * Updates the display of the current enemy on the screen.
     * This includes displaying the enemy's image, health bar, and name.
     * If the game is in a boss fight, the image size is adjusted.
     *
     * @param isBossFight Boolean indicating whether the current fight is a boss fight.
     */
    private void updateEnemyDisplay(boolean isBossFight)
    {
        if(enemies == null || enemies.isEmpty())
        {
            return;
        }

        Enemy currentEnemy = enemies.get(currentEnemyIndex);

        Image enemyImage = new Image(currentEnemy.getImageLocation());
        ImageView enemyImageView = new ImageView(enemyImage);
        enemyImageView.setFitWidth(ENEMY_IMAGE_WIDTH);
        enemyImageView.setFitHeight(ENEMY_IMAGE_HEIGHT);

        if(isBossFight){
            System.out.println("TESTT");
            enemyImageView.setFitWidth(600);
            enemyImageView.setFitHeight(600);


        }

        // Update the enemy health bar and name for the current enemy
        enemyHealthBar.updateHealth(currentEnemy.getHealth() / PROGRESS_BAR_MAX_PERCENT);
        enemyInfoText.setText(currentEnemy.getName());

        // Clear any previous enemy image and display the new one
        if(!enemyBox.getChildren().isEmpty())
        {
            enemyBox.getChildren().removeIf(node -> node instanceof ImageView);
        }
        enemyBox.getChildren().add(enemyImageView);
    }

    /**
     * Starts a timer for the word typing challenge.
     * The time limit is calculated based on the length of the word and the current game level.
     * The timer triggers the `handleTimerExpiration` method once the time limit is reached.
     * Additionally, the method starts a word movement animation that moves the word from top to bottom of the screen.
     */
    private void startWordTimer()
    {
        if (wordTimer != null) {
            wordTimer.stop();
        }

        double timeLimit = Math.max(
                currentWord.length() * MIN_TIME_PER_LETTER,
                currentWord.length() * (BASE_TIME_PER_LETTER - SCALING_FACTOR * gameLevel.getCurrentLevel())
        );

        wordTimer = new PauseTransition(Duration.seconds(timeLimit));
        wordTimer.setOnFinished(event -> handleTimerExpiration());
        wordTimer.play();

        startWordMovementAnimation(timeLimit); // Start the word movement animation
    }


    private void stopTimer()
    {
        if(wordTimer != null)
        {
            wordTimer.stop();
            wordMovementAnimation.stop(); // Stop the animation
        }
    }

    /**
     * Starts the animation that moves the word from the top to the bottom of the screen.
     * The animation duration is determined by the word's time limit.
     *
     * @param timeLimit The time limit in seconds for the animation based on the word length and game level.
     */
    private void startWordMovementAnimation(double timeLimit)
    {
        if (wordMovementAnimation != null) {
            wordMovementAnimation.stop(); // Stop any existing animation
        }

        wordMovementAnimation = new TranslateTransition(Duration.seconds(timeLimit), wordToTypeText);
        wordMovementAnimation.setFromY(0); // Start at the top of the VBox
        wordMovementAnimation.setToY(gameBox.getHeight() - wordToTypeText.getBoundsInParent().getHeight()); // Move to the bottom
        wordMovementAnimation.setInterpolator(javafx.animation.Interpolator.LINEAR); // Smooth movement
        wordMovementAnimation.setOnFinished(event -> handleTimerExpiration()); // Handle when animation finishes
        wordMovementAnimation.play();
    }

    /**
     * Handles the expiration of the word typing timer.
     * When the timer expires, the player loses health based on the current enemy's damage,
     * and the game checks if the player is defeated.
     * If the player is still alive, a new word is loaded and the timer is restarted.
     */
    private void handleTimerExpiration()
    {
        final Enemy currentEnemy = enemies.get(currentEnemyIndex);

        playerHealth -= currentEnemy.getDamage();
        playerHealthBar.updateHealth(playerHealth / PROGRESS_BAR_MAX_PERCENT);

        if (playerHealth <= 0) {
            gameOver(false); // Trigger game over for losing
            return;
        }

        currentWord = WordLoader.getRandomWord(javaTerms);
        wordToTypeText.setText(currentWord);
        resetTextFieldsForNewWord();
        clearTextFields();
    }

    /**
     * Resets the text fields for a new word.
     * This method clears any previous text entries, reinitializes the text fields based on the new word's length,
     * and resets the word's position on the screen. It also restarts the timer and animates the word's movement.
     */
    private void resetTextFieldsForNewWord()
    {
        // Clear any existing text fields in the layout
        textEntryBox.getChildren().clear();

        // Reinitialize the textFields array based on the new word length
        textFields = new TextField[currentWord.length()];

        // Create new text fields for the new word and add them to the layout
        createTextFields();

        // Enable text fields and reset the text (clear previous entries if any)
        setTextFieldsDisabled(false);
        clearTextFields();
        wordToTypeText.setTranslateY(0); // Reset position to the top

        textFields[0].requestFocus();
        startWordTimer();
    }

    /**
     * Clears the text in all the text fields used for typing the current word.
     */
    private void clearTextFields()
    {
        for(final TextField textField : textFields)
        {
            textField.clear();
        }
    }

    /**
     * Handles the player's input for a character in the current word.
     * Checks if the typed character matches the expected character in the word.
     * If correct, the focus moves to the next character input field.
     * If incorrect, the player loses health. If the word is completed, the enemy takes damage.
     * If the enemy is defeated, the next enemy is loaded. The game also checks for game over conditions.
     *
     * @param index The index of the character in the word being typed.
     */
    private void handleCharacterInput(final int index)
    {
        final String typedChar = textFields[index].getText();
        final Enemy currentEnemy = enemies.get(currentEnemyIndex);

        if(typedChar.length() == 1)
        {
            final char typedLetter = Character.toLowerCase(typedChar.charAt(ZEROTH_INDEX));
            final char correctLetter = Character.toLowerCase(currentWord.charAt(index));

            if(typedLetter == correctLetter && index < currentWord.length() - 1)
            {
                textFields[index + 1].requestFocus();
            }

            if(typedLetter != correctLetter)
            {
                stopTimer();
                setTextFieldsDisabled(true);
                playerHealth -= currentEnemy.getDamage();
                playerHealthBar.updateHealth(playerHealth / PROGRESS_BAR_MAX_PERCENT);
            }

            if(isWordCompleted())
            {
                stopTimer();
                setTextFieldsDisabled(true);
                currentEnemy.takeDamage(PLAYER_DAMAGE_AMOUNT);
                if(currentEnemy.isDefeated())
                {
                    System.out.println("DEFEATED TEST");
                    nextEnemy();
                }

                double healthPercentage = (double) currentEnemy.getHealth() / currentEnemy.getMaxHealth();
                enemyHealthBar.updateHealth(healthPercentage);


            }

            PauseTransition pause = getPause(typedLetter, correctLetter);
            pause.play();
        }

        // Check for game over
        if(playerHealth <= 0)
        {
            gameOver(false);  // Player lost
        }
        else if(isBossFight() && currentEnemy.isDefeated())  // Boss defeated
        {
            gameOver(true);   // Player won
        }
    }

    /**
     * Ends the game with a victory or defeat message based on the result.
     * Displays an alert with a message that reflects whether the player won or lost the game.
     * Hides the game window after the message is displayed.
     *
     * @param isVictory Boolean indicating whether the player won (true) or lost (false).
     */
    private void gameOver(final boolean isVictory)
    {
        stopTimer();
        final String message;
        if (isVictory) {
            message = "Congratulations! You defeated the Boss and won the game!";
        } else {
            message = "You were slain on level " + gameLevel.getCurrentLevel() + " by " + enemies.get(currentEnemyIndex).getName() + ".";
        }

        // Run the alert on the JavaFX application thread after layout and animation are finished
        Platform.runLater(() -> {
            final Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(isVictory ? "Victory" : "Defeat");
            alert.setHeaderText(isVictory ? "You Win!" : "Game Over");
            alert.setContentText(message);
            alert.showAndWait();

            // Hide the stage after alert is closed
            primaryStage.hide();
        });
    }

    /**
     * Returns a pause transition for handling incorrect character input or word completion.
     * The pause is used to reset the word, text fields, and start the next word timer after a delay.
     *
     * @param typedLetter The letter typed by the player.
     * @param correctLetter The correct letter expected for the current position in the word.
     * @return A PauseTransition that delays the resetting of the word and text fields.
     */
    private PauseTransition getPause(final char typedLetter, final char correctLetter)
    {
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(_ -> {
            if(typedLetter != correctLetter || isWordCompleted())
            {
                currentWord = WordLoader.getRandomWord(javaTerms);
                wordToTypeText.setText(currentWord);
                resetTextFieldsForNewWord();
                clearTextFields();
                textFields[ZEROTH_INDEX].requestFocus();
                setTextFieldsDisabled(false);
            }
        });
        return pause;
    }

    /**
     * Checks whether the current enemy is the boss.
     * The boss is considered the last enemy in the list.
     *
     * @return Boolean indicating whether the current fight is a boss fight.
     */
    private boolean isBossFight() {
        return currentEnemyIndex == enemies.size() - 1;  // Last enemy is the boss
    }

    /**
     * Enables or disables the text fields used for typing the current word.
     *
     * @param disabled Boolean indicating whether the text fields should be disabled (true) or enabled (false).
     */
    private void setTextFieldsDisabled(boolean disabled)
    {
        for(TextField textField : textFields)
        {
            textField.setDisable(disabled);
        }
    }

    /**
     * Checks whether the current word has been fully typed.
     * The method verifies if each character typed matches the corresponding character in the word.
     * If the word is completed, it stops the word timer and animation.
     *
     * @return Boolean indicating whether the word has been completed (true) or not (false).
     */
    private boolean isWordCompleted()
    {
        for(int i = ZEROTH_INDEX; i < currentWord.length(); i++)
        {
            if(!textFields[i].getText().equalsIgnoreCase(String.valueOf(currentWord.charAt(i))))
            {
                return false;
            }
        }
        stopTimer();
        return true;
    }
}