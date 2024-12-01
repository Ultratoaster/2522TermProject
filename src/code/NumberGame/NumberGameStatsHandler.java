package NumberGame;

/**
 * Handles the statistics for the Number Game.
 * It tracks the total games played, wins, losses, successful placements,
 * and calculates the average placements per game.
 *
 * @author Ben Henry
 * @version 1.0
 */
public class NumberGameStatsHandler {

    private static final int INITIAL_VALUE_FOR_ALL = 0;

    private int totalGames = 0;
    private int totalWins = 0;
    private int totalLosses = 0;
    private int totalPlacements = 0;

    /**
     * Returns the total number of games played.
     *
     * @return the total number of games played
     */
    public int getTotalGames() {
        return totalGames;
    }

    /**
     * Returns the total number of wins.
     *
     * @return the total number of wins
     */
    public int getTotalWins() {
        return totalWins;
    }

    /**
     * Returns the total number of losses.
     *
     * @return the total number of losses
     */
    public int getTotalLosses() {
        return totalLosses;
    }

    /**
     * Returns the total number of placements made across all games.
     *
     * @return the total number of placements
     */
    public int getTotalPlacements() {
        return totalPlacements;
    }

    /**
     * Returns the average number of placements per game.
     *
     * @return the average number of placements per game
     */
    public double getAveragePlacements() {
        return (double) totalPlacements / totalGames;
    }

    /**
     * Records a win with the given number of successful placements.
     *
     * @param placements the number of successful placements in the game
     */
    public void recordWin(int placements) {
        totalWins++;
        totalGames++;
        totalPlacements += placements;
    }

    /**
     * Records a loss with the given number of successful placements.
     *
     * @param placements the number of successful placements in the game
     */
    public void recordLoss(int placements) {
        totalLosses++;
        totalGames++;
        totalPlacements += placements;
    }

    /**
     * Returns a formatted string containing the game statistics summary.
     *
     * @return the formatted statistics string
     */
    public String getStatistics() {
        System.out.println(totalGames + " " + totalWins + " " + totalLosses + " " + totalPlacements);
        return String.format(
                "Games Played: %d | Wins: %d\nLosses: %d | Placements: %d | Avg/Game: %.2f",
                totalGames, totalWins, totalLosses, totalPlacements, getAveragePlacements()
        );
    }

    /**
     * Resets all statistics to zero for a fresh start.
     */
    public void resetStats() {
        totalGames = INITIAL_VALUE_FOR_ALL;
        totalWins = INITIAL_VALUE_FOR_ALL;
        totalLosses = INITIAL_VALUE_FOR_ALL;
        totalPlacements = INITIAL_VALUE_FOR_ALL;
    }
}
