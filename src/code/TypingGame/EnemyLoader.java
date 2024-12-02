package TypingGame;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class EnemyLoader extends LevelObserver
{
    private static int currentLevel = 1;  // Static level tracker

    // Load enemies from the enemies.txt file in the resources folder
    public static List<Enemy> loadEnemies() throws IOException {
        List<Enemy> enemies = new ArrayList<>();

        // Get the path to the "enemies.txt" file in the "resources" folder
        Path filePath = Path.of("src", "resources", "enemies.txt");  // Updated path

        // Use try-with-resources to read the file
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");  // Split by comma
                if (parts.length == 3) {
                    String name = parts[0].trim();  // TypingGame.Enemy name
                    String imageLocation = parts[1].trim();  // Full path to image in resources
                    int health = Integer.parseInt(parts[2].trim());  // Health value

                    enemies.add(new Enemy(name, health, imageLocation));
                }
            }
        }



        return enemies;
    }

    // Update the current level based on the game level
    @Override
    public void updateLevel(final int level) {
        System.out.println("Current Level: " + currentLevel);
        currentLevel = level;  // Set the currentLevel to the observed level
    }
}
