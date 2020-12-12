package sample;

import java.io.Serializable;
import java.util.*;

public class Player implements Serializable {
    private int totalStars;
    private final String playerName;
    private final Map<Integer, String> highScores;
    private GameState currentState;
    private final ArrayList<GameState> savedGames;
    private final Set<Integer> ballTypes;

    public Player(String name)  {
        totalStars = 0;
        playerName = name;
        highScores = new HashMap<Integer, String>();
        currentState = new GameState();
        savedGames = new ArrayList<GameState>();
        ballTypes = new HashSet<Integer>();
    }

    public ArrayList<GameState> getSavedGames() {
        return savedGames;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Map<Integer, String> getHighScores() { return highScores; }

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
