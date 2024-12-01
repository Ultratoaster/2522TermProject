package TypingGame;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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

public class TypingGameGUI extends Application implements LevelObserver
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
    private BackgroundImageController backgroundImageController;



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
    public static final String CHARACTER_INPUT_STYLE = "-fx-background-color: transparent; -fx-border-width: 0 0 2px 0; -fx-border-color: black; -fx-font-size: 14px;";
    public static final String ENEMY_TEXT_STYLE = "-fx-font-size: 30px;";
    private TextField[] textFields;
    private Text enemyInfoText;
    private Text wordToTypeText;

    // Gameplay elements
    public static final int PLAYER_DAMAGE_AMOUNT = 100;
    private int playerHealth = 100;
    private List<String> javaTerms;
    private List<Enemy> enemies;
    private int currentEnemyIndex;
    private String currentWord;
    private GameLevel gameLevel;

    public static final int ZEROTH_INDEX = 0;

    VBox root;
    Image backgroundImage;
    BackgroundImage bgImage;

    VBox enemyBox;
    VBox gameBox;
    VBox playerBox;
    HBox textEntryBox;

    @Override
    public void start(Stage primaryStage) {

        Platform.setImplicitExit(false);

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
        gameLevel.addObserver(this);  // Register TypingGame.TypingGameGUI as observer for TypingGame.GameLevel
        gameLevel.addObserver(backgroundImageController);

        loadEnemies();

        for (Enemy enemy : enemies) {
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

        Scene scene = new Scene(root, 600, DEFAULT_SCENE_HEIGHT_PX);
        primaryStage.setTitle("Typing Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        startGame();
    }

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
        gameBox.getChildren().add(wordToTypeText);

        playerBox = new VBox(VBOX_SPACING);
        textEntryBox = new HBox(VBOX_SPACING);
        playerBox.setAlignment(Pos.CENTER);
        textEntryBox.setAlignment(Pos.CENTER);

        textFields = new TextField[currentWord.length()];
    }

    private void loadEnemies()
    {
        try {
            enemies = EnemyLoader.loadEnemies();
        } catch (IOException e) {
            System.err.println("Failed to load enemies. Please check the enemies data file or path.");
            e.printStackTrace();
        }
    }

    private void loadWords()
    {
        try {
            javaTerms = WordLoader.loadWords("src/resources/terms.txt");  // Load words from the file
        } catch (IOException e) {
            System.err.println("Error loading words from file");
            e.printStackTrace();
        }
    }

    // New method to reset text fields for the new word
    private void resetTextFieldsForNewWord() {
        // Clear any existing text fields in the layout
        textEntryBox.getChildren().clear();

        // Reinitialize the textFields array based on the new word length
        textFields = new TextField[currentWord.length()];

        // Create new text fields for the new word and add them to the layout
        createTextFields();

        // Enable text fields and reset the text (clear previous entries if any)
        setTextFieldsDisabled(false);
        clearTextFields();

        // Set focus on the first text field
        textFields[0].requestFocus();
    }

    private void createTextFields()
    {
        for (int i = ZEROTH_INDEX; i < currentWord.length(); i++) {
            TextField textField = getTextField(i);

            textFields[i] = textField;  // Store the text field in the array
            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.TAB) {
                    textField.requestFocus();
                }
            });
            textEntryBox.getChildren().add(textField);  // Add it to the layout
        }
    }

    private TextField getTextField(final int i) {
        TextField textField;
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




    private void handleCharacterInput(final int index) {
        final String typedChar = textFields[index].getText();
        final Enemy currentEnemy = enemies.get(currentEnemyIndex);

        if (typedChar.length() == 1) {
            final char typedLetter = Character.toLowerCase(typedChar.charAt(ZEROTH_INDEX));
            final char correctLetter = Character.toLowerCase(currentWord.charAt(index));

            if (typedLetter == correctLetter && index < currentWord.length() - 1) {
                textFields[index + 1].requestFocus();
            }

            if (typedLetter != correctLetter) {
                setTextFieldsDisabled(true);
                playerHealth -= currentEnemy.getDamage();
                playerHealthBar.updateHealth(playerHealth / PROGRESS_BAR_MAX_PERCENT);
            }

            if (isWordCompleted()) {
                setTextFieldsDisabled(true);
                currentEnemy.takeDamage(PLAYER_DAMAGE_AMOUNT);
                double healthPercentage = (double) currentEnemy.getHealth() / currentEnemy.getMaxHealth();
                enemyHealthBar.updateHealth(healthPercentage);

                // Check if the enemy is defeated
                if (currentEnemy.getHealth() <= 0) {
                    nextEnemy();
                }
            }

            PauseTransition pause = getPause(typedLetter, correctLetter);
            pause.play();
        }
    }

    private PauseTransition getPause(final char typedLetter, final char correctLetter)
    {
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(_ -> {
            if (typedLetter != correctLetter || isWordCompleted()) {
                currentWord = WordLoader.getRandomWord(javaTerms);
                wordToTypeText.setText(currentWord);
                resetTextFieldsForNewWord();
                clearTextFields();
                textFields[ZEROTH_INDEX].requestFocus();
                setTextFieldsDisabled(false);
                  // Reset the text fields for the new word
            }
        });
        return pause;
    }

    private void nextEnemy() {
        currentEnemyIndex++;

        System.out.println(currentEnemyIndex);
        System.out.println(enemies.size());

        if (currentEnemyIndex == enemies.size())
        {

            // All enemies defeated, level up and restart the cycle
            gameLevel.nextLevel();  // Let TypingGame.GameLevel handle level increase

            updateBackground();
            currentEnemyIndex = ZEROTH_INDEX;
        }
        updateEnemyDisplay();
    }

    // This is called when the level changes
    @Override
    public void updateLevel(int level) {
        // Optionally: Change background or update word
        System.out.println("Level " + level);
    }

    private void setTextFieldsDisabled(boolean disabled) {
        for (TextField textField : textFields) {
            textField.setDisable(disabled);
        }
    }

    private boolean isWordCompleted() {
        for (int i = ZEROTH_INDEX; i < currentWord.length(); i++) {
            if (!textFields[i].getText().equalsIgnoreCase(String.valueOf(currentWord.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    private void clearTextFields() {
        for (TextField textField : textFields) {
            textField.clear();
        }
    }

    private void startGame() {
        updateEnemyDisplay();
    }

    private void updateEnemyDisplay() {
        if (enemies == null || enemies.isEmpty()) {
            return;
        }

        // Get the current enemy
        final Enemy currentEnemy = enemies.get(currentEnemyIndex);

        Image enemyImage = new Image(currentEnemy.getImageLocation());
        ImageView enemyImageView = new ImageView(enemyImage);
        enemyImageView.setFitWidth(ENEMY_IMAGE_WIDTH);
        enemyImageView.setFitHeight(ENEMY_IMAGE_HEIGHT);
        enemyBox.getChildren().add(enemyImageView);



        // Update the enemy health bar
        enemyHealthBar.updateHealth(currentEnemy.getHealth() / PROGRESS_BAR_MAX_PERCENT);

        // Update the enemy information text
        enemyInfoText.setText(currentEnemy.getName());

//         Remove the old enemy image if any and add the new one
        if (!enemyBox.getChildren().isEmpty()) {
//             Assuming enemyBox contains the old ImageView, we will clear it and set the new one.
            enemyBox.getChildren().removeIf(node -> node instanceof ImageView);
        }

//         Add the new image to the enemy box
        enemyBox.getChildren().add(enemyImageView);
    }

    private void updateBackground(){
        if (!root.getChildren().isEmpty()) {
//             Assuming enemyBox contains the old ImageView, we will clear it and set the new one.
            root.getChildren().removeIf(node -> node instanceof ImageView);
        }
        System.out.println(backgroundImageController.getBackground());
        backgroundImage = new Image(backgroundImageController.getBackground());
        bgImage = new BackgroundImage(backgroundImage, null, null, null, BackgroundSize.DEFAULT);
        root.setBackground(new Background(bgImage));
    }
}