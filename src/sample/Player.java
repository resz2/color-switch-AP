package sample;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Player implements Serializable {
    private int totalStars;
    private final String playerName;
    private final ArrayList<Integer> highScores;
    private GameState currentState;
    private final ArrayList<GameState> savedGames;
    private final Set<Integer> ballTypes;

    public Player(String name)  {
        totalStars = 0;
        playerName = name;
        highScores = new ArrayList<Integer>();
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

    public ArrayList<Integer> getHighScores() { return highScores; }

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
