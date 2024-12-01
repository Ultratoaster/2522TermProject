package TypingGame;

import java.io.*;
import java.util.*;

public class WordLoader {

    // This method will read the words from the file and return them as a List of strings
    public static List<String> loadWords(String filename) throws IOException {
        List<String> words = new ArrayList<>();
        BufferedReader reader = null;

        try {
            // Open the file for reading
            reader = new BufferedReader(new FileReader(filename));
            String line;

            // Read each line (word) and add it to the list
            while ((line = reader.readLine()) != null) {
                words.add(line.trim());  // Add word to list, trim any excess whitespace
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return words;
    }

    // This method returns a random word from the list of words
    public static String getRandomWord(List<String> words) {
        Random random = new Random();
        return words.get(random.nextInt(words.size()));  // Select a random word
    }
}
