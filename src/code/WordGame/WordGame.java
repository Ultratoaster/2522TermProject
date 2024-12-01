package WordGame;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class WordGame
{
    private static final int QUESTION_COUNT = 10;
    private static final int QUESTION_TYPE_COUNT = 3;
    private static final int CAPITAL_TO_COUNTRY = 0;
    private static final int COUNTRY_TO_CAPITAL = 1;
    private static final int FACT_TO_COUNTRY = 2;
    private static final int ZERO = 0;
    private static final String SCORE_PATH = "src\\score.txt";
    public static final String PLAY_AGAIN_INPUT_TEXT = "yes";
    public static final String DO_NOT_PLAY_AGAIN_INPUT_TEXT = "no";


    private static int totalGamesPlayed = ZERO;
    private static int totalFirstAttemptCorrect = ZERO;
    private static int totalSecondAttemptCorrect = ZERO;
    private static int totalIncorrectAnswers = ZERO;


    private final World world;


    public WordGame(final World world)
    {
        this.world = world;
    }


    public void playGame()
    {
        Random rand = new Random();
        int correctFirstAttempt = ZERO;
        int correctSecondAttempt = ZERO;
        int incorrectAnswers = ZERO;

        // Get all country names from the WordGame.World object (keys of countriesMap)
        List<String> countryNames = new ArrayList<>(world.getCountriesMap().keySet());

        // Ask 10 random questions
        for(int i = 0; i < QUESTION_COUNT; i++)
        {
            // Choose a random country from the WordGame.World object using the country names list
            String countryName = countryNames.get(rand.nextInt(countryNames.size()));
            Country country = world.getCountry(countryName);

            // Randomly choose a question type (A, B, or C)
            int questionType = rand.nextInt(QUESTION_TYPE_COUNT);

            String answer = "";
            String correctAnswer = "";

            // Type A: Given the capital, guess the country
            if(questionType == CAPITAL_TO_COUNTRY)
            {
                String capital = country.getCapitalCityName();
                System.out.println("What country is the capital " + capital + " the capital of?");
                answer = getUserInput();
                correctAnswer = country.getName();
            }
            // Type B: Given the country, guess the capital
            else if(questionType == COUNTRY_TO_CAPITAL)
            {
                String countryNameDisplay = country.getName();
                System.out.println("What is the capital of " + countryNameDisplay + "?");
                answer = getUserInput();
                correctAnswer = country.getCapitalCityName();
            }
            // Type C: Given a fact, guess the country
            else if(questionType == FACT_TO_COUNTRY)
            {
                String fact = country.getFacts()[rand.nextInt(country.getFacts().length)];
                System.out.println("Which country is described by this fact: " + fact);
                answer = getUserInput();
                correctAnswer = country.getName();
            }

            // Check the user's first answer
            if(answer.equalsIgnoreCase(correctAnswer))
            {
                System.out.println("CORRECT");
                correctFirstAttempt++;
            } else
            {
                // Ask for a second guess if the first is wrong
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

        // Update overall stats
        totalGamesPlayed++;
        totalFirstAttemptCorrect += correctFirstAttempt;
        totalSecondAttemptCorrect += correctSecondAttempt;
        totalIncorrectAnswers += incorrectAnswers;

        // Ask if they want to play again
        askToPlayAgain();
    }

    // Method to ask the user if they want to play again and handle input
    private void askToPlayAgain()
    {
        Scanner scanner = new Scanner(System.in);
        String response;

        while(true)
        {
            System.out.print("\nDo you want to play again? (Yes/No): ");
            response = scanner.nextLine().trim().toLowerCase();

            if(response.equals(PLAY_AGAIN_INPUT_TEXT))
            {
                playGame();
                break;
            } else if(response.equals(DO_NOT_PLAY_AGAIN_INPUT_TEXT))
            {
                handleHighScore();
                resetGameStatistics();
                break;
            } else
            {
                System.out.printf("Invalid response. Please enter '%s' or '%s'.%s", PLAY_AGAIN_INPUT_TEXT, DO_NOT_PLAY_AGAIN_INPUT_TEXT, System.lineSeparator());
            }
        }
    }

    // Method to handle the high score logic
    private void handleHighScore()
    {
        final Score currentScore = new Score(LocalDateTime.now(), totalGamesPlayed, totalFirstAttemptCorrect,
                totalSecondAttemptCorrect, totalIncorrectAnswers);
        final List<Score> previousScores = Score.readScoresFromFile(SCORE_PATH);

        final Score highestScore = getHighestScore(previousScores);

        if(currentScore.getAverageScore() > highestScore.getAverageScore())
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            System.out.printf("CONGRATULATIONS! You are the new high score with an average of %.2f points per game; " +
                            "the previous record was %.2f points per game on %s%s",
                    currentScore.getAverageScore(), highestScore.getAverageScore(),
                    highestScore.getDateTimePlayed().format(formatter), System.lineSeparator());

            // Append the current score to the file
        } else
        {
            // Inform the user of the current high score
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            System.out.printf("You did not beat the high score of %.2f points per game from %s%s",
                    highestScore.getAverageScore(), highestScore.getDateTimePlayed().format(formatter), System.lineSeparator());
        }

        // Append the current score to the file
        Score.appendScoreToFile(currentScore, SCORE_PATH);

        // Print overall stats
        printOverallStats();
    }


    // Method to find the highest score from a list
    private Score getHighestScore(final List<Score> scores)
    {
        final int ZERO_SCORE = 0;
        Score highestScore = new Score(LocalDateTime.MIN, 0, 0, 0, 0);
        for(final Score score : scores)
        {
            if(score.getAverageScore() > highestScore.getAverageScore())
            {
                highestScore = score;
            }
        }
        return highestScore;
    }


    // Method to print the overall statistics
    private void printOverallStats()
    {
        System.out.printf(
                "Overall Game Stats:%s" +
                        "%d word game(s) played%s" +
                        "%d correct answers on the first attempt%s" +
                        "%d correct answers on the second attempt%s" +
                        "%d incorrect answers on two attempts each%s",
                System.lineSeparator(),
                totalGamesPlayed, System.lineSeparator(),
                totalFirstAttemptCorrect, System.lineSeparator(),
                totalSecondAttemptCorrect, System.lineSeparator(),
                totalIncorrectAnswers, System.lineSeparator()
        );

    }

    private void resetGameStatistics()
    {
        totalGamesPlayed = ZERO;
        totalFirstAttemptCorrect = ZERO;
        totalSecondAttemptCorrect = ZERO;
        totalIncorrectAnswers = ZERO;
    }

    // Method to get user input
    private String getUserInput()
    {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().trim();
    }
}
