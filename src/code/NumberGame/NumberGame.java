package NumberGame;

import javafx.application.Application;
import javafx.application.Platform;
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
public class NumberGame extends Application
{

    private static final String GAME_OVER_INVALID_PLACEMENT_STATUS_TEXT = "Invalid placement! Game Over.";
    private static final String STAGE_TITLE_TEXT = "Number Game";
    private static final String QUIT_BUTTON_TEXT = "Quit";
    private static final String TRY_AGAIN_BUTTON_TEXT = "Try Again";
    private static final String INITIAL_SCORE_TEXT = "Successful Placements: 0";
    private static final String GAME_STATUS_IN_PROGRESS_TEXT = "Game Status: In Progress";
    private static final String CURRENT_NUMBER_TEXT = "Current number: ";
    private static final String GAME_OVER_ALERT_TITLE = "Game Over";
    private static final String YOU_WIN_ALERT_HEADER_TEXT = "You Win!";
    private static final String YOU_LOSE_ALERT_HEADER_TEXT = "You Lose!";
    private static final String PLAYER_STAT_ALERT_TEXT_FORMAT = "Placements: %d | Total Placements: %d" + System.lineSeparator() + "Wins: %d | Losses: %d | Avg Placements: %.2f";
    private static final String GAME_OVER_HEADER_TEXT = "Game Over! Here are your stats:";

    private static final int SCENE_VBOX_WIDTH = 500;
    private static final int SCENE_VBOX_HEIGHT = 750;
    private static final int VBOX_PADDING = 10;
    private static final int BUTTON_GRID_X_COUNT = 5;
    private static final int BUTTON_GRID_Y_COUNT = 5;
    private static final int BUTTON_MIN_WIDTH = 80;
    private static final int BUTTON_MIN_HEIGHT = 80;
    private static final int BUTTON_GRID_X_GAP = 10;
    private static final int BUTTON_GRID_Y_GAP = 10;

    private GameEventHandler gameEventHandler;
    private NumberGameStatsHandler numberGameStatsHandler;
    private Text currentNumberText;
    private Text gameStatusText;
    private Text scoreText;
    private Stage primaryStage;

    static final int BUTTON_COUNT = 20;
    private final Button[] buttons = new Button[BUTTON_COUNT];

    /**
     * Initializes the game and UI elements.
     *
     * @param primaryStage the primary stage of the application
     */
    @Override
    public void start(final Stage primaryStage)
    {

        Platform.setImplicitExit(false);
        this.primaryStage = primaryStage;
        gameEventHandler = new NumberPlacementGameHandler();
        numberGameStatsHandler = new NumberGameStatsHandler();

        final GridPane grid;
        final VBox vbox;
        final Button tryAgainButton;
        final Button quitButton;
        final Scene scene;


        grid = new GridPane();
        grid.setHgap(BUTTON_GRID_X_GAP);
        grid.setVgap(BUTTON_GRID_Y_GAP);
        grid.setAlignment(Pos.CENTER);

        vbox = new VBox(VBOX_PADDING);
        vbox.setAlignment(Pos.CENTER);

        tryAgainButton = new Button(TRY_AGAIN_BUTTON_TEXT);
        tryAgainButton.setOnAction(_ -> handleTryAgain());

        quitButton = new Button(QUIT_BUTTON_TEXT);
        quitButton.setOnAction(_ -> handleQuit());

        scene = new Scene(vbox, SCENE_VBOX_WIDTH, SCENE_VBOX_HEIGHT);

        currentNumberText = new Text(CURRENT_NUMBER_TEXT + gameEventHandler.currentNumber);
        gameStatusText = new Text(GAME_STATUS_IN_PROGRESS_TEXT);
        scoreText = new Text(INITIAL_SCORE_TEXT);

        for(int i = 0; i < BUTTON_COUNT; i++)
        {
            final int position = i;
            buttons[i] = new Button(" ");
            buttons[i].setMinSize(BUTTON_MIN_WIDTH, BUTTON_MIN_HEIGHT);
            buttons[i].setOnAction(_ -> handleButtonClick(position));
            grid.add(buttons[i], i % BUTTON_GRID_X_COUNT, i / BUTTON_GRID_Y_COUNT);
        }

        vbox.getChildren().addAll(currentNumberText, gameStatusText, scoreText, grid, tryAgainButton, quitButton);

        gameEventHandler.startGame();
        updateUI();


        primaryStage.setTitle(STAGE_TITLE_TEXT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Returns the formatted score text.
     *
     * @return the formatted score string
     */
    private String getFormattedScore()
    {
        return String.format(PLAYER_STAT_ALERT_TEXT_FORMAT,
                gameEventHandler.successfulPlacements, numberGameStatsHandler.getTotalPlacements(),
                numberGameStatsHandler.getTotalWins(), numberGameStatsHandler.getTotalLosses(),
                numberGameStatsHandler.getAveragePlacements());
    }

    /**
     * Checks if the win condition is met.
     * The win condition is achieved when the number of successful placements
     * equals the total number of buttons on the grid.
     *
     * @return true if the win condition is met, false otherwise
     */
    private boolean isWinConditionMet()
    {
        return gameEventHandler.successfulPlacements == BUTTON_COUNT;
    }

    /**
     * Handles a button click event. Places the number on the grid and checks if the game is over.
     *
     * @param position the position of the clicked button
     */
    private void handleButtonClick(final int position)
    {
        if(gameEventHandler.validPositions.contains(position))
        {
            buttons[position].setText(String.valueOf(gameEventHandler.currentNumber));
            gameEventHandler.placeNumberOnGrid(position);

            if(isWinConditionMet())
            {
                numberGameStatsHandler.recordWin(gameEventHandler.successfulPlacements);
                updateUI();
                showResultAlert(true); // Show win alert
                return;
            }

            gameEventHandler.generateNextNumber();
            updateUI();

            if(gameEventHandler.checkGameOver())
            {
                numberGameStatsHandler.recordLoss(gameEventHandler.successfulPlacements);
                updateUI();
                showResultAlert(false);
            }
        }
        else
        {
            gameStatusText.setText(GAME_OVER_INVALID_PLACEMENT_STATUS_TEXT);
            numberGameStatsHandler.recordLoss(gameEventHandler.successfulPlacements);
            updateUI();
            showResultAlert(false);
        }
    }


    /**
     * Creates and displays an alert with the specified title, header, and player's stats.
     *
     * @param title  the title of the alert
     * @param header the header text of the alert
     */
    private void showAlertWithStats(final String title, final String header)
    {
        final Alert alert = getAlert(title, header);
        alert.showAndWait();
    }

    /**
     * Displays the result alert (win or loss) along with the player's stats.
     * Provides options to try again or quit the game.
     *
     * @param won true if the player won, false if the player lost
     */
    private void showResultAlert(final boolean won) {
        final Alert alert = getAlert(GAME_OVER_ALERT_TITLE, won ? YOU_WIN_ALERT_HEADER_TEXT : YOU_LOSE_ALERT_HEADER_TEXT);

        final ButtonType tryAgainButton = new ButtonType(TRY_AGAIN_BUTTON_TEXT);
        final ButtonType quitButton = new ButtonType(QUIT_BUTTON_TEXT);

        alert.getButtonTypes().setAll(tryAgainButton, quitButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == tryAgainButton) {
                handleTryAgain();
            } else if (response == quitButton) {
                primaryStage.hide();
            }
        });
    }

    /**
     * Creates an alert with the specified title and header text,
     * and populates it with the player's game statistics.
     *
     * @param title  the title of the alert
     * @param header the header text of the alert
     * @return the configured Alert instance
     */
    private Alert getAlert(final String title, final String header)
    {
        final Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(String.format(PLAYER_STAT_ALERT_TEXT_FORMAT,
                gameEventHandler.successfulPlacements, numberGameStatsHandler.getTotalPlacements(),
                numberGameStatsHandler.getTotalWins(), numberGameStatsHandler.getTotalLosses(),
                numberGameStatsHandler.getAveragePlacements()));
        return alert;
    }


    /**
     * Displays an alert with the current game statistics.
     */
    private void showStatsAlert()
    {
        showAlertWithStats(GAME_OVER_ALERT_TITLE, GAME_OVER_HEADER_TEXT);
    }


    /**
     * Resets the game and the UI for a new round.
     */
    private void handleTryAgain()
    {
        gameEventHandler.resetGame();

        for(final Button button : buttons)
        {
            button.setText(" ");
        }

        gameStatusText.setText(GAME_STATUS_IN_PROGRESS_TEXT);

        updateUI();
    }


    /**
     * Updates the UI elements (current number, game status, and score).
     */
    private void updateUI()
    {
        currentNumberText.setText(CURRENT_NUMBER_TEXT + gameEventHandler.currentNumber);
        scoreText.setText(getFormattedScore());
    }

    /**
     * Handles the quit action, showing the stats alert before closing the game.
     */
    private void handleQuit()
    {
        showStatsAlert();
        primaryStage.hide();
    }

}
