import java.sql.Date;
import java.util.ArrayList;

import static java.lang.Math.ceil;

public class Score {
    public ArrayList<Time> bestTimes = new ArrayList<>();
    public int gamesPlayed = 0;
    public int gamesWon = 0;
    public int longestWinningStreak = 0;
    public int longestLosingStreak = 0;
    public int currentStreak = 0;
    public int currentWinningStreak = 0;
    public int currentLosingStreak = 0;

    public void resetScore() {
        gamesPlayed = 0;
        gamesWon = 0;
        currentStreak = 0;
        longestLosingStreak = 0;
        longestWinningStreak = 0;
        currentWinningStreak = 0;
        currentLosingStreak = 0;
    }

    public void addTime(int time, Date date) {
        bestTimes.add(new Time(time, date));
        bestTimes.sort((a, b) -> Integer.compare(a.time, b.time));
        if (bestTimes.size() > 5) {
            bestTimes.remove(bestTimes.size() - 1);
        }
    }

    public int getWinPercentage() {
        return gamesPlayed != 0 ? (int) ceil((gamesWon / gamesPlayed) * 100) : 0;
    }

    public void incGamesWon() {
        gamesWon++;
    }

    public void incGamesPlayed() {
        gamesPlayed++;
    }

    public void incCurrentStreak() {
        currentStreak++;
    }

    public void incCurrentLosingStreak() {
        currentLosingStreak++;
        if (longestLosingStreak < currentLosingStreak)
            longestLosingStreak = currentLosingStreak;
    }

    public void incCurrentWinningStreak() {
        currentWinningStreak++;
        if (longestWinningStreak < currentWinningStreak)
            longestWinningStreak = currentWinningStreak;
    }

    public void decCurrentStreak() {
        currentStreak--;
    }
}
