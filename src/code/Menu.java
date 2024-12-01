import WordGame.WordGame;
import WordGame.World;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class Menu {
    public static final String TYPING_GAME_ARGUMENT_STRING = "typinggame";
    public static final String NUMBER_GAME_ARGUMENT_STRING = "numbergame";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to the Main Menu!");
            System.out.println("Press 'w' to play the Word Game");
            System.out.println("Press 't' to play the Typing Game (via JavaFX)");
            System.out.println("Press 'n' to play the Number Game (via JavaFX)");
            System.out.println("Press 'q' to quit");

            String input = scanner.nextLine().trim().toLowerCase();

            switch (input) {

                case "w":
                    System.out.println("Starting the Word game...");

                    // Initialize the WordGame.World object
                    final World world = new World();

                    // Load countries from the directory
                    final Path directoryPath = Paths.get("src", "CountryFacts");
                    world.loadCountriesFromDirectory(directoryPath);

                    // Create and start the WordGame.WordGame
                    final WordGame wordGame = new WordGame(world);
                    wordGame.playGame();
                    break;

                case "t":
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

    private static void launchJavaFX(String gameRequest) {
        CountDownLatch latch = LauncherApp.getLatch();

        Thread javafxThread = new Thread(() -> {
            if (!LauncherApp.isAppRunning()) {
                // Pass the game request to the LauncherApp
                javafx.application.Application.launch(LauncherApp.class, gameRequest);
            } else {
                System.out.println("JavaFX application is already running.");
                if (!gameRequest.isEmpty()) {
                    // Handle game request if LauncherApp is already running
                    LauncherApp.triggerGameLaunch(gameRequest);
                }
            }
        });

        javafxThread.setDaemon(true);
        javafxThread.start();

    }
}
