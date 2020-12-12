package sample;

import java.io.*;
import java.util.ArrayList;

public class Database implements Serializable {
    private static final long serialVersionUID = 42L;
    private int lastPlayer;
    private final ArrayList<Player> playerList;

    public Database()   {
        playerList = new ArrayList<Player>();
        lastPlayer = 0;
    }

    public ArrayList<Player> getPlayers() {
        return playerList;
    }

    public int getLastPlayer() {
        return lastPlayer;
    }

    public void setLastPlayer(int player) {
        this.lastPlayer = player;
    }

    public static void serialize(Database db) throws IOException  {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream("database.txt"));
            out.writeObject(db);
        }
        finally {
            out.close();
        }
    }

    public static Database deserialize() throws IOException, ClassNotFoundException {
        Database db;
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream("database.txt"));
            db = (Database)in.readObject();
        }
        catch(FileNotFoundException | NullPointerException e)  {
            db = new Database();
        } finally {
            in.close();
        }
        return db;
    }
}
