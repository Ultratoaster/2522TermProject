import NumberGame.NumberGame;
import TypingGame.TypingGameGUI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * The LauncherApp class provides a JavaFX application throughpoint to launch different games.
 * The application supports direct game launching via command-line arguments.
 *
 * @author Ben Henry
 * @version 1.0
 */
public class LauncherApp extends Application
{
    private static boolean isAppRunning = false;

    /**
     * The entry point of the JavaFX application. If the application is not already running,
     * it launches the JavaFX application.
     *
     * @param args Command-line arguments to request a specific game to launch.
     */
    public static void main(final String[] args)
    {
        if(!isAppRunning)
        {
            launch(args);
        }
        else
        {
            System.out.println("JavaFX application is already running.");
        }
    }


    /**
     * Initializes the JavaFX stage and handles the game launch if requested through
     * command-line arguments.
     *
     * @param stage The primary stage for the JavaFX application.
     */
    @Override
    public void start(final Stage stage)
    {
        isAppRunning = true;

        final Parameters params;
        params = getParameters();

        final String gameRequest;
        gameRequest = params.getRaw().getFirst().toLowerCase();

        handleGameRequest(gameRequest);
    }



    /**
     * Returns whether the JavaFX application is currently running.
     *
     * @return true if the application is running, false otherwise.
     */
    public static boolean isAppRunning()
    {
        return isAppRunning;
    }



    /**
     * Launches the Typing Game.
     */
    private void launchTypingGame()
    {
        final TypingGameGUI typingGame;
        final Stage stage;

        typingGame = new TypingGameGUI();
        stage = new Stage();

        typingGame.start(stage);
    }

    /**
     * Launches the Number Game.
     */
    private void launchNumberGame()
    {
        final NumberGame numberGame;
        final Stage stage;

        numberGame = new NumberGame();
        stage = new Stage();

        numberGame.start(stage);
    }

    /**
     * Handles the game request based on the command-line argument.
     *
     * @param gameRequest The requested game to launch.
     */
    private void handleGameRequest(final String gameRequest)
    {
        Platform.runLater(() -> {
            switch(gameRequest)
            {
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
     * Triggers the launch of a game based on the given game request.
     *
     * @param gameRequest The requested game to launch.
     */
    public static void triggerGameLaunch(final String gameRequest)
    {
        if(isAppRunning)
        {
            Platform.runLater(() -> {
                switch(gameRequest)
                {
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

    /**
     * Stops the JavaFX application and releases the latch.
     */
    @Override
    public void stop()
    {
        isAppRunning = false;
        System.out.println("JavaFX application stopped.");
    }
}
