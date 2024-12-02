package TypingGame;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Utility class for loading and retrieving words for the typing game.
 *
 * @author Ben Henry
 * @version 1.0
 */
class WordLoader
{

    /**
     * Loads a list of words from a file specified by the given {@link Path}.
     * Each line in the file is treated as a single word.
     *
     * @param filename the {@link Path} to the file containing the words.
     * @return a {@link List} of words read from the file.
     * @throws IOException if an I/O error occurs while reading the file.
     */
    static List<String> loadWords(Path filename) throws IOException {
        final List<String> words = new ArrayList<>();
        try (final BufferedReader reader = Files.newBufferedReader(filename)) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.trim());
            }
        }
        return words;
    }

    /**
     * Retrieves a random word from the provided list of words.
     *
     * @param words The list of words to choose from.
     * @return A randomly selected word.
     */
    static String getRandomWord(final List<String> words)
    {
        final Random random;
        random = new Random();
        return words.get(random.nextInt(words.size()));
    }
}
