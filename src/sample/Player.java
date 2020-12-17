package sample;

import java.io.Serializable;
import java.util.*;

public class Player implements Serializable {
    private int totalStars;
    private final Set<Integer> gameStateIDs;
    private final String playerName;
    private final ArrayList<int[]> highScores;
    private final ArrayList<GameState> savedGames;
    private final Set<Integer> ballTypes;
    private GameState currentState;

    public Player(String name)  {
        totalStars = 0;
        playerName = name;
        gameStateIDs = new HashSet<Integer>();
        highScores = new ArrayList<int[]>();
        savedGames = new ArrayList<GameState>();
        ballTypes = new HashSet<Integer>();
        currentState = new GameState();
    }

    @Override
    public String toString() {
        String s = "  " + this.playerName;
        for(int i=0;i<20;i++)   {
            if(i>this.playerName.length())  {
                s += " ";
            }
        }
        s += this.totalStars;
        return s;
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

    public void increaseTotalStars(int totalStars) {
        this.totalStars += totalStars;
    }

    public Set<Integer> getGameStateIDs() {
        return gameStateIDs;
    }
}
