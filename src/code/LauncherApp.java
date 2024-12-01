import NumberGame.NumberGame;
import TypingGame.TypingGameGUI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.concurrent.CountDownLatch;

/**
 * The LauncherApp class provides a JavaFX application to launch different games.
 * It manages the display of a launcher menu and allows the user to choose between
 * different games (Typing Game and Number Game). The application supports direct
 * game launching via command-line arguments.
 *
 * @author Ben Henry
 * @version 1.0
 */
public class LauncherApp extends Application {

    private static Stage primaryStage;
    private static boolean isAppRunning = false;
    private static boolean isWindowHidden = false;
    public static final int INITIAL_LATCH_VALUE = 1;
    private static final CountDownLatch latch = new CountDownLatch(INITIAL_LATCH_VALUE);


    /**
     * The entry point of the JavaFX application. If the application is not already running,
     * it launches the JavaFX application.
     *
     * @param args Command-line arguments to request a specific game to launch.
     */
    public static void main(String[] args) {
        if (!isAppRunning) {
            launch(args);
        } else {
            System.out.println("JavaFX application is already running.");
        }
    }

    /**
     * Initializes the JavaFX stage and handles the game launch if requested through
     * command-line arguments. If no arguments are provided, it shows the launcher menu.
     *
     * @param stage The primary stage for the JavaFX application.
     */
    @Override
    public void start(Stage stage) {
        isAppRunning = true;
        primaryStage = stage;

        Parameters params = getParameters();
        if (!params.getRaw().isEmpty()) {
            String gameRequest = params.getRaw().getFirst().toLowerCase();
            handleGameRequest(gameRequest);
            latch.countDown();
            return;
        }
        latch.countDown();
    }


    /**
     * Launches the Typing Game.
     */
    private void launchTypingGame() {
        TypingGameGUI typingGame = new TypingGameGUI();
        typingGame.start(new Stage());
        setWindowHidden(true);
    }

    /**
     * Launches the Number Game.
     */
    private void launchNumberGame() {
        NumberGame numberGame = new NumberGame();
        numberGame.start(new Stage());
        setWindowHidden(true);
    }

    /**
     * Handles the game request based on the command-line argument.
     *
     * @param gameRequest The requested game to launch.
     */
    private void handleGameRequest(final String gameRequest) {
        Platform.runLater(() -> {
            switch (gameRequest) {
                case Menu.TYPING_GAME_ARGUMENT_STRING:
                    launchTypingGame();
                    break;
                case Menu.NUMBER_GAME_ARGUMENT_STRING:
                    launchNumberGame();
                    break;
                default:
                    System.out.println("Invalid game request: " + gameRequest);
            }
        });
    }

    /**
     * Stops the JavaFX application and releases the latch.
     */
    @Override
    public void stop() {
        isAppRunning = false;
        latch.countDown();
        System.out.println("JavaFX application stopped.");
    }

    /**
     * Returns whether the JavaFX application is currently running.
     *
     * @return true if the application is running, false otherwise.
     */
    public static boolean isAppRunning() {
        return isAppRunning;
    }

    /**
     * Returns whether the main window is currently hidden.
     *
     * @return true if the window is hidden, false otherwise.
     */
    public static boolean isWindowHidden() {
        return isWindowHidden;
    }

    /**
     * Sets the hidden status of the JavaFX window.
     *
     * @param isHidden true to hide the window, false to show it.
     */
    private static void setWindowHidden(boolean isHidden) {
        isWindowHidden = isHidden;
    }

    /**
     * Returns the CountDownLatch used to synchronize the application startup.
     *
     * @return the CountDownLatch instance.
     */
    public static CountDownLatch getLatch() {
        return latch;
    }

    /**
     * Triggers the launch of a game based on the given game request.
     *
     * @param gameRequest The requested game to launch.
     */
    public static void triggerGameLaunch(String gameRequest) {
        if (isAppRunning) {
            Platform.runLater(() -> {
                switch (gameRequest) {
                    case Menu.TYPING_GAME_ARGUMENT_STRING:
                        new TypingGameGUI().start(new Stage());
                        break;
                    case Menu.NUMBER_GAME_ARGUMENT_STRING:
                        new NumberGame().start(new Stage());
                        break;
                    default:
                        System.out.println("Invalid game request: " + gameRequest);
                }
            });
        }
    }
}
