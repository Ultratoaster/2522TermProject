package TypingGame;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class EnemyLoader {

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


                    // Create and add the new TypingGame.Enemy to the list
                    enemies.add(new Enemy(name, health, imageLocation));
                }
            }
        }

        return enemies;
    }
}