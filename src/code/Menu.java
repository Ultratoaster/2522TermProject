import WordGame.WordGame;
import WordGame.World;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * The Menu class provides a text-based interface to allow the user to select and play different games.
 * It offers options to play the Word Game, Typing Game, or Number Game, and allows the user to quit the application.
 * The Typing and Number games are launched via JavaFX.
 */
public class Menu
{

    public static final Path FACTS_DIRECTORY_PATH = Paths.get("src", "CountryFacts");
    static final String TYPING_GAME_ARGUMENT_STRING = "typinggame";
    static final String NUMBER_GAME_ARGUMENT_STRING = "numbergame";

    /**
     * The entry point of the application. Displays the main menu and processes user input
     * to launch the selected game or quit the application.
     *
     * @param args Command-line arguments (not used in this version).
     */
    public static void main(final String[] args)
    {
        Scanner scanner;
        boolean running;

        scanner = new Scanner(System.in);
        running = true;

        while(running)
        {
            System.out.print("""
                    Welcome to the Main Menu!
                    Press 'w' to play the Word Game
                    Press 'm' to play the Typing Game (via JavaFX)
                    Press 'n' to play the Number Game (via JavaFX)
                    Press 'q' to quit
                    """);


            final String input = scanner.nextLine().trim().toLowerCase();

            switch(input)
            {
                case "w":
                    System.out.println("Starting the Word game...");
                    startWordGame();
                    break;
                case "m":
                    System.out.println("Launching the Typing Game...");
                    launchJavaFX(TYPING_GAME_ARGUMENT_STRING);
                    break;
                case "n":
                    System.out.println("Launching the Number Game...");
                    launchJavaFX(NUMBER_GAME_ARGUMENT_STRING);
                    break;
                case "q":
                    System.out.println("Quitting the application...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid input. Try again.");
                    break;
            }
        }

        scanner.close();
        System.out.println("Goodbye!");
    }

    private static void startWordGame()
    {
        final World world = new World();
        world.loadCountriesFromDirectory(FACTS_DIRECTORY_PATH);

        final WordGame wordGame = new WordGame(world);
        wordGame.playGame();
    }


    /**
     * Launches the JavaFX application and passes the selected game request to the LauncherApp.
     * If the JavaFX application is already running, the requested game is launched in the existing application.
     *
     * @param gameRequest The requested game to launch, either {@value TYPING_GAME_ARGUMENT_STRING} or {@value NUMBER_GAME_ARGUMENT_STRING}.
     */
    private static void launchJavaFX(final String gameRequest)
    {
        final Thread javafxThread = new Thread(() -> {
            if(!LauncherApp.isAppRunning())
            {
                javafx.application.Application.launch(LauncherApp.class, gameRequest);
            } else
            {
                System.out.println("JavaFX application is already running.");
                if(!gameRequest.isEmpty())
                {
                    LauncherApp.triggerGameLaunch(gameRequest);
                }
            }
        });

        javafxThread.setDaemon(true);
        javafxThread.start();
    }
}
