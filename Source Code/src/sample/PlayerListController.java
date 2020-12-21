package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class PlayerListController {
    @FXML
    private ListView<Player> playerList;
    @FXML
    private AnchorPane playerListBG;

    @FXML
    public void initialize()    {
        if(Main.getDB().getPlayers().isEmpty())   {
            System.out.println("empty players");
        }
        else    {
            for(Player p: Main.getDB().getPlayers())   {
                playerList.getItems().add(p);
            }
        }
    }

    @FXML
    public void selectPlayer() {
        if(!playerList.getItems().isEmpty()) {
            Player p = playerList.getSelectionModel().getSelectedItem();
            try {
                Main.setCurrentPlayer(p);
                Database db = Main.getDB();
                db.setLastPlayer(db.getPlayers().indexOf(p));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            goBack();
        }
    }

    public void addPlayer() {
        AnchorPane pane = null;
        try {
            pane = FXMLLoader.load(getClass().getResource("newPlayer.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        playerListBG.getChildren().setAll(pane);
    }

    public void goBack() {
        AnchorPane pane = null;
        try {
            pane = FXMLLoader.load(getClass().getResource("titleScreen.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        playerListBG.getChildren().setAll(pane);
    }
}
