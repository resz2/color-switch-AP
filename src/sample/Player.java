package sample;

import javafx.util.Pair;
import java.io.Serializable;
import java.util.*;

public class Player implements Serializable {
    private int totalStars;
    private final String playerName;
    private final ArrayList<int[]> highScores;
    private GameState currentState;
    private final ArrayList<GameState> savedGames;
    private final Set<Integer> ballTypes;

    public Player(String name)  {
        totalStars = 0;
        playerName = name;
        currentState = new GameState();
        highScores = new ArrayList<int[]>();
        savedGames = new ArrayList<GameState>();
        ballTypes = new HashSet<Integer>();
    }

    public ArrayList<GameState> getSavedGames() {
        return savedGames;
    }

    public String getPlayerName() {
        return playerName;
    }

    public ArrayList<int[]> getHighScores() { return highScores; }

    public Set<Integer> getBallTypes() { return ballTypes; }

    public GameState getCurrentState() { return currentState; }

    public void setCurrentState(GameState currentState) {
        this.currentState = currentState;
    }

    public int getTotalStars() { return totalStars; }

    public void setTotalStars(int totalStars) {
        this.totalStars = totalStars;
    }
}