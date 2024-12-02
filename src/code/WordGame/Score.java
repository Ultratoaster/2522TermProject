package WordGame;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

class Score {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final int POINTS_PER_FIRST_ATTEMPT = 2;
    private static final int ZERO_GAMES_PLAYED = 0;
    public static final int EMPTY_SCORE_DATA_LENGTH = 0;
    private final LocalDateTime dateTimePlayed;
    private final int numGamesPlayed;
    private final int numCorrectFirstAttempt;
    private final int numCorrectSecondAttempt;
    private final int numIncorrectTwoAttempts;
    private final int totalScore;
    private final double averageScore;

    // Constructor to initialize score details
    Score(final LocalDateTime dateTimePlayed, final int numGamesPlayed, final int numCorrectFirstAttempt,
                 final int numCorrectSecondAttempt, final int numIncorrectTwoAttempts) {
        this.dateTimePlayed = dateTimePlayed;
        this.numGamesPlayed = numGamesPlayed;
        this.numCorrectFirstAttempt = numCorrectFirstAttempt;
        this.numCorrectSecondAttempt = numCorrectSecondAttempt;
        this.numIncorrectTwoAttempts = numIncorrectTwoAttempts;
        // Calculate total score (2 points for first attempts, 1 for second attempts)
        this.totalScore = (numCorrectFirstAttempt * POINTS_PER_FIRST_ATTEMPT) + numCorrectSecondAttempt;
        // Calculate average score
        this.averageScore = numGamesPlayed > ZERO_GAMES_PLAYED ? (double) totalScore / numGamesPlayed : ZERO_GAMES_PLAYED;
    }

    LocalDateTime getDateTimePlayed() {
        return dateTimePlayed;
    }

    double getAverageScore() {
        return averageScore;
    }

    int getScore(){
        return totalScore;
    }

    static void appendScoreToFile(final Score score, final String filePathString) {
        Path filePath = Paths.get(filePathString);
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(score.formatForFile());
        } catch (final IOException e) {
            System.err.println("Error writing to score file: " + e.getMessage());
        }
    }

    static List<Score> readScoresFromFile(final String filePathString) {
        List<Score> scores = new ArrayList<> ();
        Path filePath = Paths.get(filePathString);

        try {
            if (Files.exists(filePath)) {
                List<String> fileLines = Files.readAllLines(filePath);
                StringBuilder scoreData = new StringBuilder();
                for (final String line : fileLines) {
                    scoreData.append(line).append("\n");

                    if (line.isEmpty()) {
                        scores.add(Score.getScoreObject (scoreData.toString()));
                        scoreData.setLength(EMPTY_SCORE_DATA_LENGTH);
                    }
                }
            }
        } catch (final IOException e) {
            System.err.println("Error reading score file: " + e.getMessage());
        }
        return scores;
    }

    String formatForFile() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return String.format("""
                        Date and Time: %s
                        Total Games Played: %d
                        Correct answers on the first attempt: %d
                        Correct answers on the second attempt: %d
                        Incorrect answers: %d
                        Total Score: %d
                        Average Score: %.2f
                        
                        """,
                dateTimePlayed.format(formatter), numGamesPlayed, numCorrectFirstAttempt, numCorrectSecondAttempt,
                numIncorrectTwoAttempts, totalScore, averageScore);
    }


    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return String.format("""
                        Date and Time: %s
                        Games Played: %d
                        Correct First Attempts: %d
                        Correct Second Attempts: %d
                        Incorrect Attempts: %d
                        Score: %d points
                        """,
                dateTimePlayed.format(formatter), numGamesPlayed, numCorrectFirstAttempt, numCorrectSecondAttempt,
                numIncorrectTwoAttempts, totalScore);
    }



    static Score getScoreObject(final String scoreString) {

        final int DATE_TIME_PLAYED_INDEX = 0;
        final int NUM_GAMES_PLAYED_INDEX = 1;
        final int NUM_CORRECT_FIRST_ATTEMPT_INDEX = 2;
        final int NUM_CORRECT_SECOND_ATTEMPT_INDEX = 3;
        final int NUM_INCORRECT_TWO_ATTEMPTS_INDEX = 4;
        final int EXPECTED_LINES_COUNT = 7;  // The expected number of lines in the score string
        final String SPLIT_DELIMITER_REGEX = ": ";
        final String LINE_BREAK = "\n";

        final int SPLIT_LIMIT = 2;
        final int SPLIT_INDEX = 1;

        String[] lines = scoreString.split(LINE_BREAK);

        // Ensure there are enough lines in the score string
        if (lines.length < EXPECTED_LINES_COUNT) return null;

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

            // Parse the score components using constants
            LocalDateTime dateTimePlayed = LocalDateTime.parse(lines[DATE_TIME_PLAYED_INDEX].split(SPLIT_DELIMITER_REGEX, SPLIT_LIMIT)[SPLIT_INDEX], formatter);
            int numGamesPlayed = Integer.parseInt(lines[NUM_GAMES_PLAYED_INDEX].split(SPLIT_DELIMITER_REGEX, SPLIT_LIMIT)[SPLIT_INDEX]);
            int numCorrectFirstAttempt = Integer.parseInt(lines[NUM_CORRECT_FIRST_ATTEMPT_INDEX].split(SPLIT_DELIMITER_REGEX, SPLIT_LIMIT)[SPLIT_INDEX]);
            int numCorrectSecondAttempt = Integer.parseInt(lines[NUM_CORRECT_SECOND_ATTEMPT_INDEX].split(SPLIT_DELIMITER_REGEX, SPLIT_LIMIT)[SPLIT_INDEX]);
            int numIncorrectTwoAttempts = Integer.parseInt(lines[NUM_INCORRECT_TWO_ATTEMPTS_INDEX].split(SPLIT_DELIMITER_REGEX, SPLIT_LIMIT)[SPLIT_INDEX]);


            return new Score(dateTimePlayed, numGamesPlayed, numCorrectFirstAttempt, numCorrectSecondAttempt,
                    numIncorrectTwoAttempts);
        } catch (final Exception e) {
            System.err.println("Error parsing score entry: " + e.getMessage());
            return null;
        }
    }


}
