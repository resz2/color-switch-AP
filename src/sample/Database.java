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
        ObjectOutputStream out;
        out = new ObjectOutputStream(new FileOutputStream("database.txt"));
        out.writeObject(db);
    }

    public static Database deserialize() throws IOException, ClassNotFoundException {
        Database db;
        ObjectInputStream in;
        try {
            in = new ObjectInputStream(new FileInputStream("database.txt"));
            db = (Database)in.readObject();
            in.close();
        }
        catch(NullPointerException | FileNotFoundException e)  {
            db = new Database();
        }
        return db;
    }
}
