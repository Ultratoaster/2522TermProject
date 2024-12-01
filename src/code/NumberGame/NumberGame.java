package NumberGame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Pos;

/**
 * Main class for the Number Game application. It handles the game logic, UI, and interactions.
 *
 * @author Ben Henry
 * @version 1.0
 */
public class NumberGame extends Application {

    public static final String GAME_OVER_INVALID_PLACEMENT_STATUS_TEXT = "Invalid placement! Game Over.";
    public static final String STAGE_TITLE_TEXT = "Number Game";
    public static final String QUIT_BUTTON_TEXT = "Quit";
    public static final String TRY_AGAIN_BUTTON_TEXT = "Try Again";
    public static final String INITIAL_SCORE_TEXT = "Successful Placements: 0";
    public static final String GAME_STATUS_IN_PROGRESS_TEXT = "Game Status: In Progress";
    public static final String CURRENT_NUMBER_TEXT = "Current number: ";
    public static final String GAME_OVER_ALERT_TITLE = "Game Over";
    public static final String YOU_WIN_ALERT_HEADER_TEXT = "You Win!";
    public static final String YOU_LOSE_ALERT_HEADER_TEXT = "You Lose!";
    public static final String PLAYER_STAT_ALERT_TEXT_FORMAT = "Placements: %d | Total Placements: %d\nWins: %d | Losses: %d | Avg Placements: %.2f";
    public static final String GAME_OVER_HEADER_TEXT = "Game Over! Here are your stats:";

    public static final int SCENE_VBOX_WIDTH = 500;
    public static final int SCENE_VBOX_HEIGHT = 750;
    public static final int VBOX_PADDING = 10;
    public static final int BUTTON_GRID_X_COUNT = 5;
    public static final int BUTTON_GRID_Y_COUNT = 5;
    public static final int BUTTON_MIN_WIDTH = 80;
    public static final int BUTTON_MIN_HEIGHT = 80;
    public static final int BUTTON_COUNT = 20;
    public static final int BUTTON_GRID_X_GAP = 10;
    public static final int BUTTON_GRID_Y_GAP = 10;

    private final Button[] buttons = new Button[BUTTON_COUNT];
    private GameEventHandler gameEventHandler;
    private NumberGameStatsHandler numberGameStatsHandler;
    private Text currentNumberText;
    private Text gameStatusText;
    private Text scoreText;
    private Stage primaryStage;

    /**
     * Initializes the game and UI elements.
     *
     * @param primaryStage the primary stage of the application
     */
    @Override
    public void start(final Stage primaryStage) {
        this.primaryStage = primaryStage;
        gameEventHandler = new NumberPlacementGameHandler();
        numberGameStatsHandler = new NumberGameStatsHandler();

        GridPane grid = new GridPane();
        grid.setHgap(BUTTON_GRID_X_GAP);
        grid.setVgap(BUTTON_GRID_Y_GAP);
        grid.setAlignment(Pos.CENTER);

        for (int i = 0; i < BUTTON_COUNT; i++) {
            buttons[i] = new Button(" ");
            buttons[i].setMinSize(BUTTON_MIN_WIDTH, BUTTON_MIN_HEIGHT);
            int position = i;
            buttons[i].setOnAction(_ -> handleButtonClick(position));
            grid.add(buttons[i], i % BUTTON_GRID_X_COUNT, i / BUTTON_GRID_Y_COUNT);
        }

        VBox vbox = new VBox(VBOX_PADDING);
        vbox.setAlignment(Pos.CENTER);

        currentNumberText = new Text(CURRENT_NUMBER_TEXT + gameEventHandler.currentNumber);
        gameStatusText = new Text(GAME_STATUS_IN_PROGRESS_TEXT);
        scoreText = new Text(INITIAL_SCORE_TEXT);

        vbox.getChildren().addAll(currentNumberText, gameStatusText, scoreText, grid);

        Button tryAgainButton = new Button(TRY_AGAIN_BUTTON_TEXT);
        tryAgainButton.setOnAction(e -> handleTryAgain());

        Button quitButton = new Button(QUIT_BUTTON_TEXT);
        quitButton.setOnAction(e -> handleQuit());

        vbox.getChildren().addAll(tryAgainButton, quitButton);

        gameEventHandler.startGame();
        updateUI();

        Scene scene = new Scene(vbox, SCENE_VBOX_WIDTH, SCENE_VBOX_HEIGHT);
        primaryStage.setTitle(STAGE_TITLE_TEXT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Handles a button click event. Places the number on the grid and checks if the game is over.
     *
     * @param position the position of the clicked button
     */
    private void handleButtonClick(final int position) {
        if (gameEventHandler.validPositions.contains(position)) {
            buttons[position].setText(String.valueOf(gameEventHandler.currentNumber));
            gameEventHandler.placeNumberOnGrid(position);

            gameEventHandler.generateNextNumber();
            updateUI();

            if (gameEventHandler.checkGameOver()) {
                numberGameStatsHandler.recordLoss(gameEventHandler.successfulPlacements);
                updateUI();
                showResultAlert(false);
            }
        } else {
            gameStatusText.setText(GAME_OVER_INVALID_PLACEMENT_STATUS_TEXT);
            numberGameStatsHandler.recordLoss(gameEventHandler.successfulPlacements);
            updateUI();
            showResultAlert(false);
        }
    }

    /**
     * Displays the result alert (win or loss) along with the player's stats.
     *
     * @param won true if the player won, false if the player lost
     */
    private void showResultAlert(final boolean won) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(GAME_OVER_ALERT_TITLE);

        if (won) {
            alert.setHeaderText(YOU_WIN_ALERT_HEADER_TEXT);
        } else {
            alert.setHeaderText(YOU_LOSE_ALERT_HEADER_TEXT);
        }

        alert.setContentText(String.format(PLAYER_STAT_ALERT_TEXT_FORMAT,
                gameEventHandler.successfulPlacements, numberGameStatsHandler.getTotalPlacements(),
                numberGameStatsHandler.getTotalWins(), numberGameStatsHandler.getTotalLosses(),
                numberGameStatsHandler.getAveragePlacements()));

        ButtonType tryAgainButton = new ButtonType(TRY_AGAIN_BUTTON_TEXT);
        ButtonType quitButton = new ButtonType(QUIT_BUTTON_TEXT);

        alert.getButtonTypes().setAll(tryAgainButton, quitButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == tryAgainButton) {
                handleTryAgain();
            } else if (response == quitButton) {
                primaryStage.close();
            }
        });
    }

    /**
     * Resets the game and the UI for a new round.
     */
    private void handleTryAgain() {
        gameEventHandler.resetGame();

        for (final Button button : buttons) {
            button.setText(" ");
        }

        gameStatusText.setText(GAME_STATUS_IN_PROGRESS_TEXT);

        updateUI();
    }

    /**
     * Handles the quit action, showing the stats alert before closing the game.
     */
    private void handleQuit() {
        showStatsAlert();
        primaryStage.close();
    }

    /**
     * Displays an alert with the current game statistics.
     */
    private void showStatsAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(GAME_OVER_ALERT_TITLE);
        alert.setHeaderText(GAME_OVER_HEADER_TEXT);

        alert.setContentText(String.format(PLAYER_STAT_ALERT_TEXT_FORMAT,
                gameEventHandler.successfulPlacements, numberGameStatsHandler.getTotalPlacements(),
                numberGameStatsHandler.getTotalWins(), numberGameStatsHandler.getTotalLosses(),
                numberGameStatsHandler.getAveragePlacements()));

        alert.showAndWait();
    }

    /**
     * Updates the UI elements (current number, game status, and score).
     */
    private void updateUI() {
        currentNumberText.setText(CURRENT_NUMBER_TEXT + gameEventHandler.currentNumber);
        scoreText.setText(getFormattedScore());
    }

    /**
     * Returns the formatted score text.
     *
     * @return the formatted score string
     */
    private String getFormattedScore() {
        return String.format(PLAYER_STAT_ALERT_TEXT_FORMAT,
                gameEventHandler.successfulPlacements, numberGameStatsHandler.getTotalPlacements(),
                numberGameStatsHandler.getTotalWins(), numberGameStatsHandler.getTotalLosses(),
                numberGameStatsHandler.getAveragePlacements());
    }
}
