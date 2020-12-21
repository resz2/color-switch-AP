package sample;

import java.io.Serializable;
import java.util.*;

public class Player implements Serializable {
    private int totalStars, currentBall;
    private final Set<Integer> gameStateIDs;
    private final String playerName;
    private final ArrayList<int[]> highScores;
    private final ArrayList<GameState> savedGames;
    private final boolean[] ballTypes;
    private GameState currentState;

    public Player(String name)  {
        totalStars = currentBall = 0;
        playerName = name;
        gameStateIDs = new HashSet<Integer>();
        highScores = new ArrayList<int[]>();
        savedGames = new ArrayList<GameState>();
        ballTypes = new boolean[5];
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

    public boolean[] getBallTypes() { return ballTypes; }

    public GameState getCurrentState() { return currentState; }

    public void setCurrentState(GameState currentState) {
        this.currentState = currentState;
    }

    public int getTotalStars() { return totalStars; }

    public void increaseTotalStars(int add) { this.totalStars += add; }
    public void decreaseTotalStars(int sub) { this.totalStars -= sub; }

    public Set<Integer> getGameStateIDs() { return gameStateIDs; }

    public int getCurrentBall() { return currentBall; }
    public void setCurrentBall(int currentBall) { this.currentBall = currentBall; }
}
