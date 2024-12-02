package TypingGame;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * The EnemyLoader class is responsible for loading enemy data from a file and updating the game level.
 * It implements the LevelObserver interface to observe level changes and adjust enemy characteristics.
 * <p>
 * Enemies are loaded from a text file, where each line contains an enemy's name, image location, and health.
 * The class provides functionality for loading enemies into the game and keeping track of the current game level.
 *
 * @author [Your Name]
 * @version 1.0
 */
class EnemyLoader
{
    /**
     * Loads the enemies from a text file and returns a list of Enemy objects.
     * Each line in the file should contain the enemy's name, image location, and health, separated by commas.
     *
     * @return A list of Enemy objects loaded from the file.
     * @throws IOException If an error occurs while reading the file.
     */
    static List<Enemy> loadEnemies() throws IOException
    {
        final List<Enemy> enemies;
        final Path filePath;

        enemies = new ArrayList<>();

        filePath = Path.of("src", "resources", "enemies.txt");

        final int NAME_INDEX = 0;
        final int IMAGE_LOCATION_INDEX = 1;
        final int HEALTH_INDEX = 2;
        final String LINE_SPLIT_REGEX = ",";
        final int EXPECTED_LINE_PART_COUNT = 3;

        try(final BufferedReader reader = Files.newBufferedReader(filePath))
        {
            String line;
            while((line = reader.readLine()) != null)
            {
                String[] parts = line.split(LINE_SPLIT_REGEX);
                if(parts.length == EXPECTED_LINE_PART_COUNT)
                {
                    String name = parts[NAME_INDEX].trim();
                    String imageLocation = parts[IMAGE_LOCATION_INDEX].trim();
                    int health = Integer.parseInt(parts[HEALTH_INDEX].trim());

                    enemies.add(new Enemy(name, health, imageLocation));
                }
            }
        }

        return enemies;
    }
}
