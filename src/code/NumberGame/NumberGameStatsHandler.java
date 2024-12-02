package NumberGame;

/**
 * Handles the statistics for the Number Game.
 * It tracks the total games played, wins, losses, successful placements,
 * and calculates the average placements per game.
 *
 * @author Ben Henry
 * @version 1.0
 */
class NumberGameStatsHandler
{
    private static final int ZERO_TOTAL_GAMES = 0;

    private int totalGames = 0;
    private int totalWins = 0;
    private int totalLosses = 0;
    private int totalPlacements = 0;

    /**
     * Returns the total number of wins.
     *
     * @return the total number of wins
     */
    int getTotalWins()
    {
        return totalWins;
    }

    /**
     * Returns the total number of losses.
     *
     * @return the total number of losses
     */
    int getTotalLosses()
    {
        return totalLosses;
    }

    /**
     * Returns the total number of placements made across all games.
     *
     * @return the total number of placements
     */
    int getTotalPlacements()
    {
        return totalPlacements;
    }

    /**
     * Returns the average number of placements per game.
     *
     * @return the average number of placements per game
     */
    double getAveragePlacements()
    {
        return totalGames > ZERO_TOTAL_GAMES ? (double) totalPlacements / totalGames : ZERO_TOTAL_GAMES;
    }

    /**
     * Records a win with the given number of successful placements.
     *
     * @param placements the number of successful placements in the game
     */
    void recordWin(final int placements)
    {
        totalWins++;
        totalGames++;
        totalPlacements += placements;
    }

    /**
     * Records a loss with the given number of successful placements.
     *
     * @param placements the number of successful placements in the game
     */
    void recordLoss(final int placements)
    {
        totalLosses++;
        totalGames++;
        totalPlacements += placements;
    }

    /**
     * Returns a formatted string containing the game statistics summary.
     *
     * @return the formatted statistics string
     */
    String getStatistics()
    {
        System.out.println(totalGames + " " + totalWins + " " + totalLosses + " " + totalPlacements);
        return String.format(
                "Games Played: %d | Wins: %d%sLosses: %d | Placements: %d | Avg/Game: %.2f",
                totalGames, totalWins, System.lineSeparator(), totalLosses, totalPlacements, getAveragePlacements()
        );
    }

}
