package WordGame;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * A word-based game where players answer questions about countries, capitals, and facts.
 * Tracks statistics and manages high scores across games.
 *
 * @author YourName
 * @version 1.0
 */
public class WordGame
{
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final int QUESTION_COUNT = 10;
    private static final int ZERO = 0;
    private static final int MINIMAL_SCORE = 0;
    private static final String SCORE_PATH = "src\\score.txt";
    public static final String PLAY_AGAIN_INPUT_TEXT = "yes";
    public static final String DO_NOT_PLAY_AGAIN_INPUT_TEXT = "no";

    private static int totalGamesPlayed = ZERO;
    private static int totalFirstAttemptCorrect = ZERO;
    private static int totalSecondAttemptCorrect = ZERO;
    private static int totalIncorrectAnswers = ZERO;

    private final World world;
    private final Scanner scanner;

    /**
     * Enum representing different types of questions in the game.
     */
    private enum QuestionType
    {
        CAPITAL_TO_COUNTRY,
        COUNTRY_TO_CAPITAL,
        FACT_TO_COUNTRY
    }

    /**
     * Constructs a WordGame with a given World object.
     *
     * @param world the world data used in the game
     */
    public WordGame(final World world)
    {
        this.world = world;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the word game session.
     */
    public void playGame()
    {
        final Random rand = new Random();
        int correctFirstAttempt = ZERO;
        int correctSecondAttempt = ZERO;
        int incorrectAnswers = ZERO;

        final List<String> countryNames = new ArrayList<>(world.getCountriesMap().keySet());

        for(int i = 0; i < QUESTION_COUNT; i++)
        {
            final String countryName = countryNames.get(rand.nextInt(countryNames.size()));
            final Country country = world.getCountry(countryName);
            QuestionType questionType = QuestionType.values()[rand.nextInt(QuestionType.values().length)];

            String answer = "";
            String correctAnswer = "";

            switch(questionType)
            {
                case CAPITAL_TO_COUNTRY ->
                {
                    System.out.println("What country is the capital " + country.capitalCityName() + " the capital of?");
                    answer = getUserInput();
                    correctAnswer = country.name();
                }
                case COUNTRY_TO_CAPITAL ->
                {
                    System.out.println("What is the capital of " + country.name() + "?");
                    answer = getUserInput();
                    correctAnswer = country.capitalCityName();
                }
                case FACT_TO_COUNTRY ->
                {
                    System.out.println("Which country is described by this fact: " + country.facts()[rand.nextInt(country.facts().length)]);
                    answer = getUserInput();
                    correctAnswer = country.name();
                }
            }

            if(answer.equalsIgnoreCase(correctAnswer))
            {
                System.out.println("CORRECT");
                correctFirstAttempt++;
            } else
            {
                System.out.println("INCORRECT. Try again.");
                answer = getUserInput();
                if(answer.equalsIgnoreCase(correctAnswer))
                {
                    System.out.println("CORRECT");
                    correctSecondAttempt++;
                } else
                {
                    System.out.println("The correct answer was " + correctAnswer);
                    incorrectAnswers++;
                }
            }
        }

        totalGamesPlayed++;
        totalFirstAttemptCorrect += correctFirstAttempt;
        totalSecondAttemptCorrect += correctSecondAttempt;
        totalIncorrectAnswers += incorrectAnswers;

        askToPlayAgain();
    }

    private void askToPlayAgain()
    {
        boolean continueAsking = true;

        while(continueAsking)
        {
            System.out.printf("%sDo you want to play again? (Yes/No): ", System.lineSeparator());
            String response = scanner.nextLine().trim().toLowerCase();

            if(response.equals(PLAY_AGAIN_INPUT_TEXT))
            {
                playGame();
                continueAsking = false;
            } else if(response.equals(DO_NOT_PLAY_AGAIN_INPUT_TEXT))
            {
                handleHighScore();
                resetGameStatistics();
                continueAsking = false;
            } else
            {
                System.out.printf("Invalid response. Please enter '%s' or '%s'.%s", PLAY_AGAIN_INPUT_TEXT, DO_NOT_PLAY_AGAIN_INPUT_TEXT, System.lineSeparator());
            }
        }
    }

    private void handleHighScore()
    {
        final Score currentScore = new Score(LocalDateTime.now(), totalGamesPlayed, totalFirstAttemptCorrect,
                totalSecondAttemptCorrect, totalIncorrectAnswers);
        final List<Score> previousScores = Score.readScoresFromFile(SCORE_PATH);
        final Score highestScore = getHighestScore(previousScores);

        if(currentScore.getAverageScore() > highestScore.getAverageScore())
        {
            System.out.printf("CONGRATULATIONS! You are the new high score with an average of %.2f points per game; the previous record was %.2f points per game on %s%s",
                    currentScore.getAverageScore(), highestScore.getAverageScore(),
                    highestScore.getDateTimePlayed().format(DATE_TIME_FORMATTER), System.lineSeparator());
        } else
        {
            System.out.printf("You did not beat the high score of %.2f points per game from %s%s",
                    highestScore.getAverageScore(), highestScore.getDateTimePlayed().format(DATE_TIME_FORMATTER), System.lineSeparator());
        }

        Score.appendScoreToFile(currentScore, SCORE_PATH);
        printOverallStats();
    }

    private Score getHighestScore(final List<Score> scores)
    {
        Score highestScore = new Score(LocalDateTime.MIN, MINIMAL_SCORE, MINIMAL_SCORE, MINIMAL_SCORE, MINIMAL_SCORE);
        for(final Score score : scores)
        {
            if(score.getAverageScore() > highestScore.getAverageScore())
            {
                highestScore = score;
            }
        }
        return highestScore;
    }

    private void printOverallStats()
    {
        System.out.printf("""
                        Overall Game Stats:
                        %d word game(s) played
                        %d correct answers on the first attempt
                        %d correct answers on the second attempt
                        %d incorrect answers on two attempts each
                        """,
                totalGamesPlayed,
                totalFirstAttemptCorrect,
                totalSecondAttemptCorrect,
                totalIncorrectAnswers
        );
    }

    private void resetGameStatistics()
    {
        totalGamesPlayed = ZERO;
        totalFirstAttemptCorrect = ZERO;
        totalSecondAttemptCorrect = ZERO;
        totalIncorrectAnswers = ZERO;
    }

    private String getUserInput()
    {
        return scanner.nextLine().trim();
    }
}
